package pa.gob.dntic.arkalite.solicitudes.dominio;

import java.util.List;
import java.util.Optional;

public interface RepositorioDeSolicitudes {
    void guardar(Solicitud s);
    Optional<Solicitud> buscar(String id);
    List<Solicitud> todas();

}
