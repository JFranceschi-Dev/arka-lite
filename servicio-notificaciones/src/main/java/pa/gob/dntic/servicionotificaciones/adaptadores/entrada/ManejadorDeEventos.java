package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;

public class ManejadorDeEventos {
    private final ServicioDeNotificaciones servicio;

    public ManejadorDeEventos(ServicioDeNotificaciones servicio) {
        this.servicio = servicio;
    }

    public void manejar(SolicitudEnviada evento) {
        servicio.alRecibirSolicitudEnviada(evento);
    }
}
