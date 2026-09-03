package pa.gob.dntic.arkalite.notificaciones.adaptadores.entrada;

import org.springframework.stereotype.Component;
import pa.gob.dntic.arkalite.eventos.SolicitudEnviada;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;

/* ADAPTADOR de entrada de Notificaciones: recibe el evento y llama a su servicio. */
@Component
public class ManejadorDeEventos {
    private final ServicioDeNotificaciones servicio;

    public ManejadorDeEventos(ServicioDeNotificaciones servicio) {
        this.servicio = servicio;
    }

    public void manejar(SolicitudEnviada evento) {
        servicio.alRecibirSolicitudEnviada(evento);
    }
}
