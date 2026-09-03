package pa.gob.dntic.arkalite.notificaciones.dominio;

import pa.gob.dntic.arkalite.eventos.SolicitudEnviada;
import java.util.List;

/* Dominio de Notificaciones. Reacciona al EVENTO, no al servicio de solicitudes. */
public class ServicioDeNotificaciones {
    private final RepositorioDeNotificaciones repositorio;

    public ServicioDeNotificaciones(RepositorioDeNotificaciones repositorio) {
        this.repositorio = repositorio;
    }

    public void alRecibirSolicitudEnviada(SolicitudEnviada e) {
        repositorio.guardar(new Notificacion("Solicitud " + e.id() + " (" + e.tipo() + ") enviada"));
    }

    public List<Notificacion> listar() {
        return repositorio.todas();
    }
}
