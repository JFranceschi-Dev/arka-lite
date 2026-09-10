package pa.gob.dntic.servicionotificaciones.dominio;

import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;

import java.util.List;

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
