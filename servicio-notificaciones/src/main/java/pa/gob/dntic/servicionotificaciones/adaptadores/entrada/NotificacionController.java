package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pa.gob.dntic.servicionotificaciones.dominio.Notificacion;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;

import java.util.List;

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
