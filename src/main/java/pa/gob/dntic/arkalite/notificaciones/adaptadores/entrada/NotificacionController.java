package pa.gob.dntic.arkalite.notificaciones.adaptadores.entrada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pa.gob.dntic.arkalite.notificaciones.dominio.Notificacion;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;
import java.util.List;

/* Para VER que el evento llegó: GET /notificaciones */
@RestController
public class NotificacionController {
    private final ServicioDeNotificaciones servicio;

    public NotificacionController(ServicioDeNotificaciones servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/notificaciones")
    public List<Notificacion> todas() {
        return servicio.listar();
    }
}
