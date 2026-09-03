package pa.gob.dntic.arkalite.notificaciones.dominio;


import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;

import java.util.List;

public class ServicioDeNotificaciones {

    private final RepositorioDeNotificacion repositorio;

    public ServicioDeNotificaciones(RepositorioDeNotificacion repositorio) {
        this.repositorio = repositorio;
    }

    //se debe borrar ojo prueba
    public List<Notificaciones> listar() {
        return repositorio.todas();
    }

    public void guardar(String name) {
       repositorio.Publicar(name);
    }

//    public void alRecibirSolicitudEnviada(SolicitudEnviada e) {
//        repositorio.guardar(new Notificacion("Solicitud " + e.id() + " (" + e.tipo() + ") enviada"));
//    }




}
