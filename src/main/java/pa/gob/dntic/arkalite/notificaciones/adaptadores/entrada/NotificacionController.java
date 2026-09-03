package pa.gob.dntic.arkalite.notificaciones.adaptadores.entrada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pa.gob.dntic.arkalite.notificaciones.dominio.Notificaciones;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.arkalite.solicitudes.dominio.ServicioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;

import java.util.List;

@RestController
public class NotificacionController {

    private final ServicioDeNotificaciones servicio;


    NotificacionController(ServicioDeNotificaciones servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/todasNotificaciones")
    public List<Notificaciones> todas(){
        return servicio.listar();
    }

    @GetMapping("/Notificaciones/{id}")
    public void porId (@PathVariable String id){
         servicio.guardar(id);
    }



}
