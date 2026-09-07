# Guía de Docker

## Qué es el Dockerfile del proyecto

El `Dockerfile` contiene las instrucciones para construir la imagen de ARKA-Lite. La imagen incluye Java y el archivo JAR de la aplicación. A partir de esa imagen se pueden crear uno o varios contenedores.

El archivo actual es:

```dockerfile
FROM eclipse-temurin:26-jdk
WORKDIR /app
COPY target/arkalite.jar app.jar
RUN useradd -r -u 1001 appuser && chown -R appuser /app
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Explicación línea por línea

`FROM eclipse-temurin:26-jdk`

Define la imagen base. En este caso utiliza Eclipse Temurin con el JDK de Java 26, la misma versión indicada en `pom.xml`. Todas las demás instrucciones se aplican sobre esta imagen.

`WORKDIR /app`

Crea o selecciona `/app` como carpeta de trabajo dentro de la imagen. Las rutas relativas de las instrucciones siguientes parten de esa carpeta.

`COPY target/arkalite.jar app.jar`

Copia el archivo `target/arkalite.jar` desde la computadora hacia `/app/app.jar` dentro de la imagen. Por esta razón se debe ejecutar `.\mvnw.cmd package` antes de `docker build`. El `Dockerfile` actual no compila el código.

`RUN useradd -r -u 1001 appuser && chown -R appuser /app`

Durante la construcción crea el usuario interno `appuser`, con identificador 1001, y le entrega la propiedad de `/app`. Esto permite ejecutar la aplicación sin usar el usuario administrador `root`.

`USER appuser`

Indica que las instrucciones de ejecución y el proceso de Java se ejecutarán como `appuser`. Es una medida básica de seguridad.

`EXPOSE 8080`

Documenta que la aplicación escucha en el puerto 8080 dentro del contenedor. Esta instrucción no permite el acceso desde la computadora por sí sola; el puerto se publica al ejecutar `docker run -p`.

`ENTRYPOINT ["java", "-jar", "app.jar"]`

Es el comando que se ejecuta al iniciar el contenedor. Equivale a ejecutar `java -jar app.jar` desde `/app`.

`docs/` está en `.dockerignore`, por lo que esta documentación no se envía al contexto de construcción ni aumenta la imagen.

## Construir y ejecutar

Desde la raíz del repositorio:

```powershell
# 1. Ejecutar pruebas y generar target/arkalite.jar
.\mvnw.cmd package

# 2. Construir la imagen
docker build -t arkalite:local .

# 3. Crear y ejecutar el contenedor en segundo plano
docker run --name arkalite -d -p 8080:8080 arkalite:local

# 4. Verificar
docker ps
docker logs -f arkalite
Invoke-RestMethod http://localhost:8080/solicitudes
```

El primer `8080` de `-p 8080:8080` es el puerto de la computadora; el segundo es el puerto dentro del contenedor.

## Cambiar el puerto

### Cambiar solo el puerto de acceso desde la computadora

No requiere modificar archivos ni reconstruir la imagen:

```powershell
docker run --name arkalite -d -p 9090:8080 arkalite:local
```

La aplicación se visita en `http://localhost:9090`, pero sigue escuchando en 8080 dentro del contenedor. Esta es la opción recomendada para evitar conflictos locales.

### Cambiar también el puerto interno de Spring Boot

Spring Boot acepta la variable `SERVER_PORT`:

```powershell
docker run --name arkalite -d -e SERVER_PORT=9090 -p 9090:9090 arkalite:local
```

También puede fijarse `server.port=9090` en `src/main/resources/application.properties`, aunque eso cambia el valor predeterminado para todos los entornos. Si se cambia permanentemente el puerto interno, conviene actualizar `EXPOSE 8080` a `EXPOSE 9090` en el `Dockerfile`; `EXPOSE` documenta el puerto, pero no lo publica por sí solo.

## Ciclo de vida del contenedor

```powershell
docker ps                              # Contenedores activos
docker ps -a                           # Todos los contenedores
docker stop arkalite                   # Detener de forma ordenada
docker start arkalite                  # Reiniciar uno existente
docker restart arkalite                # Detener e iniciar
docker rm arkalite                     # Eliminar uno detenido
docker rm -f arkalite                  # Forzar eliminación; interrumpe el proceso
docker rename arkalite arkalite-old    # Renombrar
docker inspect arkalite                # Configuración detallada
docker stats arkalite                  # CPU y memoria
docker top arkalite                    # Procesos
docker port arkalite                   # Puertos publicados
docker exec -it arkalite sh            # Shell, si la imagen lo permite
```

Como la persistencia actual es en memoria, detener y volver a iniciar el mismo contenedor conserva el proceso solo durante la ejecución; al reiniciar la JVM se pierde el estado. Recrear el contenedor también comienza sin solicitudes ni notificaciones.

## Registros y diagnóstico

```powershell
docker logs arkalite
docker logs --tail 100 arkalite
docker logs --since 10m arkalite
docker logs -f arkalite
docker inspect --format '{{.State.Status}}' arkalite
docker inspect --format '{{.State.ExitCode}}' arkalite
```

Problemas habituales:

- `COPY target/arkalite.jar ... no such file`: ejecutar `.\mvnw.cmd package` antes de `docker build`.
- El nombre ya existe: usar `docker rm arkalite` si está detenido o elegir otro valor en `--name`.
- El puerto ya está ocupado: publicar otro puerto, por ejemplo `-p 9090:8080`.
- El contenedor termina inmediatamente: revisar `docker logs arkalite` y `docker inspect arkalite`.
- La petición no responde: confirmar `docker ps`, `docker port arkalite` y que se usa el puerto del lado izquierdo de `-p`.

## Imágenes

```powershell
docker image ls
docker image inspect arkalite:local
docker history arkalite:local
docker tag arkalite:local registro.ejemplo/arka/arkalite:1.0.0
docker push registro.ejemplo/arka/arkalite:1.0.0
docker pull registro.ejemplo/arka/arkalite:1.0.0
docker image rm arkalite:local
docker image prune                         # Quita imágenes sin usar; revisar antes
```

Para una entrega reproducible se etiqueta con una versión inmutable, por ejemplo `1.2.0` o el SHA del commit. `latest` puede existir como alias, pero no debe ser la única referencia de producción.

## Variables, límites y reinicio

```powershell
docker run --name arkalite -d `
  -p 8080:8080 `
  -e SERVER_PORT=8080 `
  --memory 512m `
  --cpus 1 `
  --restart unless-stopped `
  arkalite:local
```

Las variables sensibles no deben escribirse en el `Dockerfile`, la línea de comandos compartida ni el repositorio. En un entorno real se usa el mecanismo de secretos de la plataforma.

## Red Docker

Cuando la aplicación necesite comunicarse con otros contenedores:

```powershell
docker network create arka-net
docker run --name arkalite --network arka-net -d -p 8080:8080 arkalite:local
docker network inspect arka-net
docker network rm arka-net
```

Dentro de una red definida por el usuario, los contenedores se encuentran por nombre; `localhost` siempre se refiere al propio contenedor.

## Limpieza segura

```powershell
docker system df             # Revisar consumo
docker container prune       # Eliminar contenedores detenidos
docker image prune           # Eliminar imágenes colgantes
docker builder prune         # Eliminar caché de construcción no usada
```

Antes de cualquier `prune`, revisar el alcance. Evitar `docker system prune --volumes` en equipos con datos locales importantes.

## Entrega recomendada

1. Ejecutar `.\mvnw.cmd package`.
2. Construir la imagen desde un commit identificado.
3. Analizar dependencias e imagen en el pipeline.
4. Etiquetar con versión y SHA, no solo con `latest`.
5. Publicar en un registro autenticado.
6. Desplegar por digest o etiqueta inmutable.
7. Verificar endpoint, logs y métricas.
8. Conservar la imagen anterior para reversión.
