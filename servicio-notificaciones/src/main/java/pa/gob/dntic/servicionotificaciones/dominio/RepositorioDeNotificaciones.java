package pa.gob.dntic.servicionotificaciones.dominio;

import java.util.List;

public interface RepositorioDeNotificaciones {
    void guardar(Notificacion n);
    List<Notificacion> todas();
}
