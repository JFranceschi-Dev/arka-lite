#!/usr/bin/env bash
# Verificador del Lab S36 (pipeline de ARKA). Córrelo desde la raiz del repo de ARKA.
#  ->  bash verificar.sh
set -u
PASS=0; TOTAL=7
WF=".github/workflows/ci.yml"
echo "== S36: pipeline de ARKA, dos servicios (7 compuertas) =="
[ -f "$WF" ] || { echo "[X] No hay $WF"; exit 1; }

echo "-- (1) matrix sobre los DOS servicios..."
if grep -qiE 'matrix' "$WF" && grep -qE 'servicio-solicitudes' "$WF" && grep -qE 'servicio-notificaciones' "$WF"; then echo "[OK] (1) matrix con ambos servicios"; PASS=$((PASS+1)); else echo "[X] (1) usa strategy.matrix con [servicio-solicitudes, servicio-notificaciones]"; fi

echo "-- (2) prueba cada servicio (working-directory)..."
if grep -qiE 'mvn .*(verify|test)' "$WF" && grep -qiE 'working-directory' "$WF"; then echo "[OK] (2) mvn verify por servicio"; PASS=$((PASS+1)); else echo "[X] (2) corre 'mvn verify' con working-directory: \${{ matrix.servicio }}"; fi

echo "-- (3) construye Y publica..."
if grep -qiE 'docker build' "$WF" && grep -qiE 'docker push|push:\s*true' "$WF"; then echo "[OK] (3) build + push"; PASS=$((PASS+1)); else echo "[X] (3) falta docker build y/o docker push"; fi

echo "-- (4) imagen POR SERVICIO (usa matrix.servicio en el tag)..."
if grep -iE 'docker build|docker push|IMG=' "$WF" | grep -qE 'matrix\.servicio'; then echo "[OK] (4) cada servicio se etiqueta con su nombre"; PASS=$((PASS+1)); else echo "[X] (4) el tag de la imagen debe incluir \${{ matrix.servicio }} (una imagen por servicio, no una sola)"; fi

echo "-- (5) credencial por SECRETS, no en texto plano..."
HARD=$(grep -iE '(password|token):[[:space:]]*[^$[:space:]]' "$WF" || true)
if grep -qE 'secrets\.' "$WF" && [ -z "$HARD" ]; then echo "[OK] (5) usa secrets, sin hardcode"; PASS=$((PASS+1)); else echo "[X] (5) autentica con \${{ secrets.X }}, nunca en texto plano"; fi

echo "-- (6) orden: probar antes de construir/publicar..."
LT=$(grep -niE 'mvn .*(verify|test)' "$WF" | head -1 | cut -d: -f1)
LB=$(grep -niE 'docker build|docker push' "$WF" | head -1 | cut -d: -f1)
if [ -n "$LT" ] && [ -n "$LB" ] && [ "$LT" -lt "$LB" ]; then echo "[OK] (6) prueba antes de empaquetar"; PASS=$((PASS+1)); else echo "[X] (6) mueve mvn verify ANTES del build/push"; fi

[ -f "./mvnw.cmd" ] || { echo "-- (mvn no instalado: la 7 corre en tu maquina)"; echo ""; echo "PUNTAJE PARCIAL: ${PASS}/6"; exit 0; }
echo "-- (7) atrapa un test roto en un servicio..."
T="servicio-solicitudes/src/test/java/pa/gob/dntic/solicitudes/SolicitudTest.java"
if [ -f "$T" ]; then
  cp "$T" /tmp/st.bak; trap 'cp /tmp/st.bak "'"$T"'" 2>/dev/null' EXIT
  sed -i 's/Estado.ENVIADA, s.estado()/Estado.BORRADOR, s.estado()/' "$T"
  if ( cd servicio-solicitudes && ./mvnw.cmd -B test >/tmp/s36.log 2>&1 ); then echo "[X] (7) el test roto PASO"; else echo "[OK] (7) con un test roto, ese servicio pone la linea en ROJO"; PASS=$((PASS+1)); fi
  cp /tmp/st.bak "$T"; trap - EXIT
else echo "[X] (7) no encontre el test de solicitudes"; fi

echo ""; echo "PUNTAJE: ${PASS}/${TOTAL}"
[ "$PASS" -eq "$TOTAL" ] && echo "Pipeline de ARKA: dos servicios, probados, publicados, en orden, con secretos, y atrapa lo roto." || echo "Aun no."
