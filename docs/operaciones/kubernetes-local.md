# Kubernetes local con Docker Desktop

Requisito: Kubernetes habilitado en Docker Desktop. Todos los comandos se ejecutan desde la raíz del repositorio.

## Levantar

```powershell
kubectl config use-context docker-desktop

docker compose build
docker tag arka-lite-servicio-notificaciones:latest servicio-notificaciones:local
docker tag arka-lite-servicio-solicitudes:latest servicio-solicitudes:local

kubectl apply -R -f k8s/
kubectl get pods
```

## Acceder

Cada comando en una terminal distinta:

```powershell
kubectl port-forward svc/servicio-solicitudes 18080:8080
kubectl port-forward svc/servicio-notificaciones 18081:8080
```

- Solicitudes: http://localhost:18080/swagger-ui/index.html
- Notificaciones: http://localhost:18081/swagger-ui/index.html

## Actualizar después de un cambio de código

Ejemplo para notificaciones. Para solicitudes, cambiar el nombre del servicio.

```powershell
docker compose build servicio-notificaciones
docker tag arka-lite-servicio-notificaciones:latest servicio-notificaciones:local
docker exec desktop-control-plane crictl rmi docker.io/library/servicio-notificaciones:local
kubectl rollout restart deploy/servicio-notificaciones
```

Si `docker exec` responde `No such container`, omitir ese comando.

Después, volver a abrir el `port-forward`.

## Logs

```powershell
kubectl logs deploy/servicio-solicitudes
kubectl logs deploy/servicio-notificaciones
```

Para detener y eliminar todo: [Limpieza de Kubernetes local](kubernetes-limpieza.md).
