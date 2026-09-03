package pa.gob.dntic.arkalite.solicitudes.adaptadores.entrada;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.arkalite.solicitudes.dominio.ServicioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;

import java.util.List;

@RestController
public class SolicitudController {
    private final ServicioDeSolicitudes servicio;
    private final ServicioDeNotificaciones servicio2;
    SolicitudController(ServicioDeSolicitudes serviciov, ServicioDeNotificaciones servicio2) {

        this.servicio = serviciov;
        this.servicio2 = servicio2;
    }

    @GetMapping("/solicitudes")
    public List<Solicitud> todas(){
        return servicio.listar();
    }



    @GetMapping("/solicitudes/{id}")
    public Solicitud porId (@PathVariable String id){
        return servicio.buscar(id);
    }

    @PostMapping("/solicitudes/{id}/aprobar")
    public Solicitud aprobar (@PathVariable String id){
        return servicio.aprobar(id);
    }

    @PostMapping("/solicitudes/{id}/rechazar")
    public Solicitud rechazar (@PathVariable String id){
        return servicio.rechazar(id);
    }


}
