package pa.gob.dntic.arkalite.solicitudes.adaptadores.salida;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;
import pa.gob.dntic.arkalite.solicitudes.dominio.SolicitudNoEncontrada;

import java.util.*;

@Repository
@Primary
public class RepositorioEnMemoria implements RepositorioDeSolicitudes {
//    private final List<Solicitud> almacen = new ArrayList<>();
    private final Map<String, Solicitud> almacen = new HashMap<>();

    public void guardar(Solicitud s) {
//        almacen.add(s);
        almacen.put(s.id(), s);
//        throw new UnsupportedOperationException("TODO: guardar()" );
    }


    public Optional<Solicitud> buscar(String id) {
        return Optional.ofNullable(almacen.get(id));

        // throw new UnsupportedOperationException("TODO: buscar()" );
    }


    public List<Solicitud> todas() {
      return almacen.values().stream().toList();

//        throw new UnsupportedOperationException("TODO: todas()" );
    }




}
