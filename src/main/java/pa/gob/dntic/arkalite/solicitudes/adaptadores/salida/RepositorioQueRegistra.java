package pa.gob.dntic.arkalite.solicitudes.adaptadores.salida;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;
import java.util.*;

/*
 * SEGUNDO adaptador del MISMO puerto: hace lo mismo que RepositorioEnMemoria
 * pero registra cada escritura. El dominio no cambia ni una línea: ese es el pago.
 */
@Repository
@Primary
public class RepositorioQueRegistra implements RepositorioDeSolicitudes {

    private final Map<String, Solicitud> almacen = new LinkedHashMap<>();

    public void guardar(Solicitud s) {
        System.out.println("[repo] guardando " + s.id() + " (" + s.estado() + ")");
        almacen.put(s.id(), s);
    }

    public Optional<Solicitud> buscar(String id) {
        return Optional.ofNullable(almacen.get(id));
    }

    public List<Solicitud> todas() {
        return new ArrayList<>(almacen.values());
    }
}