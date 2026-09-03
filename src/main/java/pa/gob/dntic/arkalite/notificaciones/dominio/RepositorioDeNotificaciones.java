package pa.gob.dntic.arkalite.notificaciones.dominio;
import java.util.List;
public interface RepositorioDeNotificaciones {
    void guardar(Notificacion n);
    List<Notificacion> todas();
}
