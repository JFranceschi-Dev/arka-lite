package pa.gob.dntic.arkalite.notificaciones.adaptadores.salida;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.notificaciones.dominio.Notificaciones;
import pa.gob.dntic.arkalite.notificaciones.dominio.RepositorioDeNotificacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RepositorioEnMemoriaNotificacion implements RepositorioDeNotificacion {

    private final Map<String, Notificaciones> notificacion = new HashMap<>();

    public void Publicar(String s) {
        notificacion.put(s, new Notificaciones(s));
//        throw new UnsupportedOperationException("TODO: guardar()" );
    }

    public List<Notificaciones> todas() {
        return notificacion.values().stream().toList();

//        throw new UnsupportedOperationException("TODO: todas()" );
    }

}
