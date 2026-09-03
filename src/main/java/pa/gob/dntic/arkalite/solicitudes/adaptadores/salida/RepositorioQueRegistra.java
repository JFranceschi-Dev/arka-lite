package pa.gob.dntic.arkalite.solicitudes.adaptadores.salida;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;

import java.util.*;

@Repository
//@Primary  // etiqueta para saber cual de los dos adaptadores tomar como principal es decir palabra reservada para decir que este es el adaptador principal
public class RepositorioQueRegistra implements RepositorioDeSolicitudes {
    private  final Map<String, Solicitud> almacen = new LinkedHashMap<>();

    public  void guardar(Solicitud s) {
        almacen.put(s.id(), s);
        System.out.println("[Repo] Solicitud guardada: " + s.id()+"("+s.estado()+")");

    }
    public Optional<Solicitud> buscar(String id) {
        return Optional.ofNullable(almacen.get(id));
    }
    public List<Solicitud> todas() {
        return new ArrayList<>(almacen.values());
    }

}
