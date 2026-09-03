package pa.gob.dntic.arkalite.solicitudes.adaptadores.salida;

import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;
import java.util.*;

/*
 * ADAPTADOR de salida: cumple el PUERTO guardando en memoria.
 */

@Repository("repositorioDeSolicitudesEnMemoria")
public class RepositorioEnMemoria implements RepositorioDeSolicitudes {

    // TODO: un Map para guardar las solicitudes por id


    public void guardar(Solicitud s) {
        //TODO
        throw new UnsupportedOperationException("TODO: guardar");
    }

    public Optional<Solicitud> buscar(String id) {
        // TODO
        throw new UnsupportedOperationException("TODO: buscar()");
    }

    public List<Solicitud> todas() {
        // TODO
        throw new UnsupportedOperationException("TODO: todas()");
    }
}
