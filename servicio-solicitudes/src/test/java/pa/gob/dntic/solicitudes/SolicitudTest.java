package pa.gob.dntic.solicitudes;

import org.junit.jupiter.api.Test;
import pa.gob.dntic.serviciosolicitudes.dominio.Estado;
import pa.gob.dntic.serviciosolicitudes.dominio.Solicitud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolicitudTest {

    @Test
    void unaSolicitudEnBorradorSePuedeEnviar() {
        Solicitud s = new Solicitud("SOL-001", "Incidencia", Estado.BORRADOR);

        s = s.enviar();

        assertEquals(Estado.ENVIADA, s.estado());
    }

    @Test
    void unaSolicitudEnviadaSePuedeAprobarORechazar() {
        Solicitud enviada = new Solicitud("SOL-002", "Cambio", Estado.BORRADOR).enviar();

        assertEquals(Estado.APROBADA, enviada.aprobar().estado());
        assertEquals(Estado.RECHAZADA, enviada.rechazar().estado());
    }

    @Test
    void unaTransicionInvalidaExplicaElEstadoRequerido() {
        Solicitud borrador = new Solicitud("SOL-003", "Reclamo", Estado.BORRADOR);

        IllegalStateException exception = assertThrows(IllegalStateException.class, borrador::aprobar);

        assertEquals("solo se aprueba una solicitud ENVIADA", exception.getMessage());
    }
}
