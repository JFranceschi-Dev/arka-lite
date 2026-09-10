package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;

@RestController
public class EventoController {
    private final ServicioDeNotificaciones servicio;
    // constructor
    public EventoController(ServicioDeNotificaciones s){ this.servicio = s; }

    // Evento
    @PostMapping("/eventos/solicitud-enviada")
    public void recibir(@RequestBody SolicitudEnviada evento){
        servicio.alRecibirSolicitudEnviada(evento);
    }
}
