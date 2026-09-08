#!/usr/bin/env bash
# Verificador del LAB S28 (imagen optimizada de ARKA). Córrelo en tu máquina,
# desde la carpeta de ARKA con tu Dockerfile multi-stage.  ->  bash verificar.sh
set -u
PASS=0; TOTAL=7
echo "== S28: optimizacion de la imagen de ARKA (7 compuertas) =="
command -v docker >/dev/null 2>&1 || { echo "[X] Docker no esta instalado"; exit 1; }
[ -f Dockerfile ] || { echo "[X] No hay Dockerfile aqui"; exit 1; }

# (1) multi-stage: >=2 FROM y un COPY --from
NFROM=$(grep -ciE '^\s*FROM ' Dockerfile)
if [ "$NFROM" -ge 2 ] && grep -qiE 'COPY\s+--from' Dockerfile; then echo "[OK] (1) multi-stage (${NFROM} etapas, con COPY --from)"; PASS=$((PASS+1)); else echo "[X] (1) no es multi-stage: necesitas >=2 FROM y un COPY --from=build"; fi

# (2) cache de dependencias: COPY del pom ANTES del COPY del src
LP=$(grep -niE 'COPY .*pom' Dockerfile | head -1 | cut -d: -f1)
LS=$(grep -niE 'COPY .*src'  Dockerfile | head -1 | cut -d: -f1)
if [ -n "$LP" ] && [ -n "$LS" ] && [ "$LP" -lt "$LS" ]; then echo "[OK] (2) el pom se copia antes que el src (dependencias cacheadas)"; PASS=$((PASS+1)); else echo "[X] (2) copia el pom y resuelve dependencias ANTES de copiar el src (si no, cada cambio re-descarga todo)"; fi

# (3) construye DESDE EL FUENTE dentro de Docker (sin jar del host)
echo "-- (3) build desde fuente (borro target/ para forzarlo)..."
rm -rf target 2>/dev/null
if docker build -t arkaopt . >/tmp/arkaopt_build.log 2>&1; then echo "[OK] (3) construye desde el fuente, dentro de Docker"; PASS=$((PASS+1)); else echo "[X] (3) build fallo (¿depende de un jar del host? ver /tmp/arkaopt_build.log)"; tail -4 /tmp/arkaopt_build.log; fi

echo "-- corriendo..."; docker rm -f arkaoptc >/dev/null 2>&1
docker run -d --name arkaoptc -p 8080:8080 arkaopt >/dev/null 2>&1; sleep 8

# (4) ambos endpoints
if curl -sf http://localhost:8080/solicitudes 2>/dev/null | grep -qi "INC-001" && curl -sf http://localhost:8080/notificaciones 2>/dev/null | grep -qi "CAM-002"; then echo "[OK] (4) /solicitudes y /notificaciones responden"; PASS=$((PASS+1)); else echo "[X] (4) uno de los endpoints no respondio"; fi

# (5) no-root
WHO=$(docker exec arkaoptc whoami 2>/dev/null || echo "")
if [ -n "$WHO" ] && [ "$WHO" != "root" ]; then echo "[OK] (5) no-root (${WHO})"; PASS=$((PASS+1)); else echo "[X] (5) corre como ROOT"; fi

# (6) imagen chica: la etapa final NO trae Maven ni fuente
SIZE=$(docker image inspect arkaopt --format '{{.Size}}' 2>/dev/null || echo 0); MB=$((SIZE/1000000))
if [ "$MB" -gt 0 ] && [ "$MB" -lt 400 ]; then echo "[OK] (6) imagen final ${MB} MB (< 400: sin Maven ni fuente en la etapa final)"; PASS=$((PASS+1)); else echo "[X] (6) imagen ${MB} MB: si es enorme, Maven/fuente se colaron a la etapa final. Revisa el multi-stage (ajusta el umbral a tu base si hace falta)"; fi

# (7) sobrevive reinicio
docker restart arkaoptc >/dev/null 2>&1; sleep 8
if curl -sf http://localhost:8080/solicitudes 2>/dev/null | grep -qi "INC-001"; then echo "[OK] (7) sobrevive 'docker restart'"; PASS=$((PASS+1)); else echo "[X] (7) no sobrevivio el reinicio"; fi

docker rm -f arkaoptc >/dev/null 2>&1
echo ""; echo "PUNTAJE: ${PASS}/${TOTAL}"
[ "$PASS" -eq "$TOTAL" ] && echo "Imagen optimizada. Sobreviviste S28." || echo "Aun no. Revisa arriba."
