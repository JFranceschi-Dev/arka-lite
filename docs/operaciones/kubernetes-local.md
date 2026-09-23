# Kubernetes local con Docker Desktop

Esta guía levanta los dos microservicios en el Kubernetes integrado de Docker Desktop y valida que `servicio-solicitudes` se comunique con `servicio-notificaciones`. Los comandos son los que se usaron para la primera prueba de los manifests de `k8s/`.

Para detener y eliminar todo lo creado aquí, consultar [Limpieza de Kubernetes local](kubernetes-limpieza.md).

## Requisitos

- Docker Desktop con Kubernetes habilitado (**Settings > Kubernetes > Enable Kubernetes**).
- `kubectl` apuntando al contexto `docker-desktop`.

```powershell
docker version --format '{{.Server.Version}}'
kubectl config current-context   # debe mostrar docker-desktop
```

Si el contexto es otro:

```powershell
kubectl config use-context docker-desktop
```

## 1. Construir las imágenes

Desde la raíz del repositorio. Compose construye ambos servicios con sus `Dockerfile`:

```powershell
docker compose build
```

Compose nombra las imágenes `arka-lite-servicio-notificaciones:latest` y `arka-lite-servicio-solicitudes:latest`. Los manifests esperan `servicio-*:local`, por eso se agrega ese tag:

```powershell
docker tag arka-lite-servicio-notificaciones:latest servicio-notificaciones:local
docker tag arka-lite-servicio-solicitudes:latest servicio-solicitudes:local
docker images | Select-String servicio
```

Docker Desktop comparte las imágenes locales con su clúster, así que no hace falta publicarlas en un registro. Los manifests usan `imagePullPolicy: IfNotPresent` para que Kubernetes use la imagen local en vez de buscarla en Docker Hub.

### Desplegar un cambio de código

El nodo de Docker Desktop (`desktop-control-plane`) guarda su propia copia de cada imagen. Como el tag `:local` no cambia y la política es `IfNotPresent`, un `rollout restart` **sigue usando la imagen vieja**. Hay que borrar la copia del nodo antes de reiniciar. Ejemplo para notificaciones:

```powershell
docker compose build servicio-notificaciones
docker tag arka-lite-servicio-notificaciones:latest servicio-notificaciones:local

docker exec desktop-control-plane crictl rmi docker.io/library/servicio-notificaciones:local
kubectl rollout restart deploy/servicio-notificaciones
kubectl rollout status deploy/servicio-notificaciones --timeout=120s
```

Para confirmar que el pod usa la imagen nueva, comparar el digest del pod con el ID local. Deben coincidir:

```powershell
kubectl get pod -l app=servicio-notificaciones -o jsonpath='{.items[0].status.containerStatuses[0].imageID}'
docker image inspect servicio-notificaciones:local --format '{{.Id}}'
```

Para ver las imágenes que tiene el nodo:

```powershell
docker exec desktop-control-plane crictl images
```

Los `port-forward` abiertos se cortan cuando se reemplaza el pod. Hay que volver a abrirlos después del reinicio.

## 2. Desplegar

`-R` aplica todos los manifests dentro de `k8s/` y sus subcarpetas:

```powershell
kubectl apply -R -f k8s/
kubectl rollout status deploy/servicio-notificaciones --timeout=120s
kubectl rollout status deploy/servicio-solicitudes --timeout=120s
kubectl get pods,svc -o wide
```

Ambos pods deben quedar en `1/1 Running`. Como los manifests no definen readiness probe, `Running` no garantiza que Spring haya terminado de arrancar. Por eso se revisan los logs.

## 3. Revisar los logs

```powershell
kubectl logs deploy/servicio-notificaciones --tail=20
kubectl logs deploy/servicio-solicitudes --tail=20
```

Cada servicio debe mostrar `Started ...Application`. En solicitudes deben aparecer los datos de ejemplo y el envío de `CAM-002`:

```text
[repo] guardando INC-001 (BORRADOR)
[repo] guardando CAM-002 (BORRADOR)
[repo] guardando CAM-002 (ENVIADA)
```

Si aparece `no se pudo notificar`, solicitudes no alcanzó a notificaciones.

## 4. Validar la comunicación

Abrir túneles hacia los Services. Cada `port-forward` ocupa una terminal; se detiene con `Ctrl+C`.

```powershell
# Terminal 1
kubectl port-forward svc/servicio-solicitudes 18080:8080

# Terminal 2
kubectl port-forward svc/servicio-notificaciones 18081:8080
```

En una tercera terminal:

```powershell
# Ya debe existir la notificación de CAM-002, enviada al arrancar
Invoke-RestMethod http://localhost:18081/notificaciones

# Crear y enviar una solicitud
Invoke-RestMethod -Method Post http://localhost:18080/solicitudes/crear
Invoke-RestMethod -Method Post http://localhost:18080/solicitudes/INC-002/enviar

# Debe aparecer la notificación de INC-002
Invoke-RestMethod http://localhost:18081/notificaciones
```

La llamada de solicitudes a notificaciones ocurre dentro del clúster, sin pasar por el `port-forward`. Para confirmar que el nombre del Service resuelve desde el pod de solicitudes:

```powershell
kubectl exec deploy/servicio-solicitudes -- getent hosts servicio-notificaciones
```

Debe devolver la `CLUSTER-IP` de `servicio-notificaciones` que muestra `kubectl get svc`.

Los Services son `NodePort`, pero en Docker Desktop el nodo corre dentro de un contenedor y su puerto no responde en `localhost`. Por eso se usa `port-forward`.

### Puertos reservados por Windows

Windows puede reservar puertos para HTTP.sys o Hyper-V. En ese caso `port-forward` falla con `bind: Intento de acceso a un socket no permitido`, o el navegador recibe `Bad Request - Invalid Hostname`. Para ver los puertos reservados:

```powershell
netsh int ipv4 show excludedportrange protocol=tcp
```

Por eso esta guía usa 18080 y 18081 en vez de 8080 y 8081, que en algunos equipos están reservados.

## 5. Validar desde el navegador

Con los dos `port-forward` del paso anterior abiertos, ambos servicios exponen Swagger UI:

| Servicio | Swagger UI | OpenAPI JSON |
| --- | --- | --- |
| Solicitudes | http://localhost:18080/swagger-ui/index.html | http://localhost:18080/v3/api-docs |
| Notificaciones | http://localhost:18081/swagger-ui/index.html | http://localhost:18081/v3/api-docs |

Para validar la comunicación sin usar la terminal:

1. En Swagger de solicitudes, ejecutar `POST /solicitudes/crear` y después `POST /solicitudes/{id}/enviar` con `INC-002`.
2. En Swagger de notificaciones, ejecutar `GET /notificaciones`.
3. Debe aparecer `Solicitud INC-002 (Incidencia) enviada`.

Si solicitudes responde `ENVIADA` pero la notificación no aparece, revisar los logs de solicitudes (paso 3).

## 6. Explorar con k9s (opcional)

k9s es una interfaz de terminal para Kubernetes. Instalación en Windows:

```powershell
winget install --id Derailed.k9s -e --source winget
```

Abrir una terminal nueva para que tome el PATH actualizado y ejecutar `k9s`. Atajos útiles: `l` muestra los logs, `s` abre una shell en el pod y `:svc` lista los Services.

## Limitación conocida

A diferencia de Compose, Kubernetes no tiene `depends_on`. Si solicitudes arranca antes que notificaciones, el envío automático de `CAM-002` falla y no se reintenta. Los envíos posteriores sí llegan. Para evitarlo se necesitaría una readiness probe en notificaciones y reintentos en `PublicadorRest`.

Volver a [Operaciones](README.md).
