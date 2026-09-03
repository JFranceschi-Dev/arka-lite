package pa.gob.dntic.arkalite.solicitudes.adaptadores.entrada;

import org.springframework.web.bind.annotation.*;
import pa.gob.dntic.arkalite.solicitudes.dominio.Estado;
import pa.gob.dntic.arkalite.solicitudes.dominio.ServicioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.Solicitud;
import java.util.List;

/*
 * ADAPTADOR de entrada: traduce la web hacia el dominio. Es DELGADO:
 * no tiene lógica ni almacenamiento; solo llama al servicio.
 */
@RestController
public class SolicitudController {

    private final ServicioDeSolicitudes servicio;

    public SolicitudController(ServicioDeSolicitudes servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/solicitudes")
    public List<Solicitud> todas() {
        return servicio.listar();
    }

    @GetMapping("/solicitudes/{id}")
    public Solicitud porId(@PathVariable String id) {
        return servicio.buscar(id);
    }

    @PostMapping("solicitudes/crear")
    public Solicitud crearNueva() {
        Solicitud nueva = new Solicitud ("INC-002", "Incidencia", Estado.BORRADOR);
        return servicio.registrar(nueva.id(), nueva.tipo());
    }

    @PostMapping("/solicitudes/{id}/enviar")
    public Solicitud enviar(@PathVariable String id) {
        return servicio.enviar(id);
    }
}
