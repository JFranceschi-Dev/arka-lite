package pa.gob.dntic.servicionotificaciones.adaptadores.salida;

import org.springframework.stereotype.Repository;
import pa.gob.dntic.servicionotificaciones.dominio.Notificacion;
import pa.gob.dntic.servicionotificaciones.dominio.RepositorioDeNotificaciones;

import java.util.ArrayList;
import java.util.List;

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
