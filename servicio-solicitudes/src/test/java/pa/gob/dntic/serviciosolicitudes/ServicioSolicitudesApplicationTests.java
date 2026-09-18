package pa.gob.dntic.serviciosolicitudes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa.gob.dntic.serviciosolicitudes.adaptadores.salida.RepositorioEnMemoria;
import pa.gob.dntic.serviciosolicitudes.dominio.Estado;
import pa.gob.dntic.serviciosolicitudes.dominio.PublicadorDeEventos;
import pa.gob.dntic.serviciosolicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.serviciosolicitudes.dominio.ServicioDeSolicitudes;
import pa.gob.dntic.serviciosolicitudes.dominio.Solicitud;
import pa.gob.dntic.serviciosolicitudes.dominio.SolicitudNoEncontrada;
import pa.gob.dntic.serviciosolicitudes.eventos.SolicitudEnviada;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServicioSolicitudesApplicationTests {

    private ServicioDeSolicitudes servicio;
    private final List<SolicitudEnviada> eventosPublicados = new ArrayList<>();

    @BeforeEach
    void onBefore() {
        RepositorioDeSolicitudes repositorio = new RepositorioEnMemoria();
        PublicadorDeEventos publicador = eventosPublicados::add;
        servicio = new ServicioDeSolicitudes(repositorio, publicador);
        eventosPublicados.clear();
    }

    @Test
    void givenUnaSolicitudEnBorrador_whenSeEnvia_thenQuedaEnviadaYPublicaEvento() {
        servicio.registrar("SOL-001", "Incidencia");
        throw new exception("Error de prueba"); // Simula un error para probar el manejo de excepciones
        Solicitud enviada = servicio.enviar("SOL-001");

        assertEquals(Estado.ENVIADA, enviada.estado());
        assertEquals(1, eventosPublicados.size());
        assertEquals("SOL-001", eventosPublicados.getFirst().id());
        assertEquals("Incidencia", eventosPublicados.getFirst().tipo());
    }

    @Test
    void givenUnaSolicitudEnviada_whenSeAprueba_thenQuedaAprobada() {
        servicio.registrar("SOL-002", "Reclamo");
        servicio.enviar("SOL-002");

        Solicitud aprobada = servicio.aprobar("SOL-002");

        assertEquals(Estado.APROBADA, aprobada.estado());
    }

    @Test
    void givenUnaSolicitudNoRegistrada_whenSeBusca_thenLanzaSolicitudNoEncontrada() {
        assertThrows(SolicitudNoEncontrada.class, () -> servicio.buscar("NO-EXISTE"));
    }
}
