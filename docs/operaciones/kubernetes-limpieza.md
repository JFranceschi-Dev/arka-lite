# Limpieza de Kubernetes local

Desde la raíz del repositorio.

## Eliminar los pods y servicios

```powershell
kubectl delete -R -f k8s/ --ignore-not-found
```

Cerrar los `port-forward` con `Ctrl+C`.

## Eliminar imágenes huérfanas

```powershell
docker image prune -f
docker builder prune -f
```

## Eliminar las imágenes del proyecto

```powershell
docker rmi servicio-solicitudes:local servicio-notificaciones:local arka-lite-servicio-solicitudes:latest arka-lite-servicio-notificaciones:latest
docker exec desktop-control-plane crictl rmi docker.io/library/servicio-solicitudes:local docker.io/library/servicio-notificaciones:local
```
