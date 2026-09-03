package pa.gob.dntic.arkalite.solicitudes.dominio;

import pa.gob.dntic.arkalite.notificaciones.dominio.RepositorioDeNotificacion;

import java.util.List;
import java.util.Optional;

public class ServicioDeSolicitudes {

    private final RepositorioDeSolicitudes repositorio;

    public ServicioDeSolicitudes(RepositorioDeSolicitudes repositorio) {

        this.repositorio = repositorio;
    }

    public  Solicitud registrar(String id , String tipo) {
        // TODO: crear una Solicitud en BORRADOR, guardarla en el repositorio y devolverla
        Solicitud solicitud = new Solicitud(id, tipo, Estado.BORRADOR);
        repositorio.guardar(solicitud);
        return solicitud;

//        throw new UnsupportedOperationException("TODO: registrar()" );
    }

    public  Solicitud enviar(String id ) {
        // TODO: buscar la solicitud, llamar a su enviar(), guardarla y devolverla

//        throw new UnsupportedOperationException("TODO: enviar()" );
        Solicitud solicitudes = buscar(id);
        Solicitud enviada = solicitudes.enviar();
//        Publicar.Publicar();

        repositorio.guardar(enviada);
        return enviada;

    }

    public  Solicitud aprobar(String id ) {
        //TODO: buscar la solicitud llamar a su enviar(), guardarla y devolverla
        Solicitud solicitudes = buscar(id);
        Solicitud enviada2 = solicitudes.aprobar();
        repositorio.guardar(enviada2);
        return enviada2;
//        throw new UnsupportedOperationException("TODO: aprobar()" );
    }

    public  Solicitud rechazar(String id )
    {
        Solicitud solicitudes = buscar(id);
        Solicitud enviada3 = solicitudes.rechazar();
        repositorio.guardar(enviada3);
        return enviada3;
//        throw new UnsupportedOperationException("TODO: rechazar()" );
    }

    public List<Solicitud> listar() {
        // TODO: devolver todas las del repositorio
        return repositorio.todas();
        // throw new UnsupportedOperationException("TODO: listar()" );
    }

    public Solicitud buscar(String id) {
        // TODO: devolver la del id, o lanzar SolicitudNoEncontrada si no está
        return repositorio.buscar(id)
                .orElseThrow(() -> new SolicitudNoEncontrada(id));
//        throw new UnsupportedOperationException("TODO: buscar()" );
    }

}
