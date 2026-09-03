package pa.gob.dntic.arkalite.notificaciones.adaptadores.salida;

import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.notificaciones.dominio.Notificacion;
import pa.gob.dntic.arkalite.notificaciones.dominio.RepositorioDeNotificaciones;
import java.util.*;

@Repository("repositorioDeNotificacionesEnMemoria")
public class RepositorioEnMemoria implements RepositorioDeNotificaciones {
    private final List<Notificacion> almacen = new ArrayList<>();

    public void guardar(Notificacion n) {
        almacen.add(n);
    }

    public List<Notificacion> todas() {
        return List.copyOf(almacen);
    }
}
