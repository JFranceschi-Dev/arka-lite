package pa.gob.dntic.arkalite.notificaciones.dominio;

import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;

import java.util.List;

public interface RepositorioDeNotificacion {
    void Publicar( String enviada);
    List<Notificaciones> todas();
}
