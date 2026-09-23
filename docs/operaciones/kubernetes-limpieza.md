# Limpieza de Kubernetes local

Esta guía elimina lo creado en [Kubernetes local con Docker Desktop](kubernetes-local.md) para que no quede nada de ARKA-Lite en ejecución.

## 1. Eliminar los recursos del clúster

Desde la raíz del repositorio. Borra los Deployments, sus pods y los Services definidos en `k8s/`:

```powershell
kubectl delete -R -f k8s/ --ignore-not-found
```

Esperar a que los pods terminen:

```powershell
kubectl wait --for=delete pod -l app=servicio-solicitudes --timeout=60s
kubectl wait --for=delete pod -l app=servicio-notificaciones --timeout=60s
```

## 2. Cerrar los túneles

Si quedó algún `kubectl port-forward` abierto, detenerlo con `Ctrl+C` en su terminal. Si se perdió la terminal:

```powershell
Get-Process kubectl -ErrorAction SilentlyContinue | Stop-Process
```

Este comando también detiene cualquier otro proceso `kubectl` que esté corriendo.

## 3. Detener Compose, si se usó

```powershell
docker compose down
```

## 4. Verificar que no queda nada

```powershell
kubectl get deploy,svc servicio-solicitudes servicio-notificaciones --ignore-not-found
kubectl get pods -l 'app in (servicio-solicitudes,servicio-notificaciones)'
docker ps --filter name=servicio
```

El primer comando no debe imprimir nada. El segundo debe responder `No resources found` y el tercero solo debe mostrar los encabezados.

## 5. Eliminar imágenes huérfanas

Cada vez que se reconstruye una imagen con el mismo tag, la versión anterior pierde su nombre y queda como `<none>` (imagen huérfana o *dangling*). No sirve para nada y ocupa disco.

Ver cuánto espacio se puede recuperar:

```powershell
docker system df
docker images -f dangling=true
```

Borrar solo las imágenes huérfanas:

```powershell
docker image prune -f
```

Las compilaciones con Docker también dejan caché de build, que suele ocupar más que las imágenes. Borrarla hace que la próxima compilación descargue de nuevo las dependencias de Maven:

```powershell
docker builder prune -f
```

`docker image prune` no toca imágenes con nombre ni imágenes en uso. Evitar `docker system prune -a`, porque borra también las imágenes de otros proyectos que no tengan un contenedor activo.

## 6. Eliminar las imágenes de ARKA-Lite (opcional)

Las imágenes no consumen CPU ni memoria, solo disco (más de 1 GB entre las dos). Para borrarlas del Docker local:

```powershell
docker rmi servicio-solicitudes:local servicio-notificaciones:local
docker rmi arka-lite-servicio-solicitudes:latest arka-lite-servicio-notificaciones:latest
```

El nodo de Kubernetes guarda su propia copia. Para borrarla también:

```powershell
docker exec desktop-control-plane crictl rmi docker.io/library/servicio-solicitudes:local docker.io/library/servicio-notificaciones:local
```

Si se borran, antes de volver a desplegar hay que repetir el paso de construcción de la guía anterior.

## 7. Apagar Kubernetes de Docker Desktop (opcional)

Para liberar los recursos del propio clúster, desactivar **Settings > Kubernetes > Enable Kubernetes** en Docker Desktop.

## 8. Desinstalar k9s (opcional)

```powershell
winget uninstall --id Derailed.k9s -e
```

Volver a [Operaciones](README.md).
