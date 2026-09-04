package pa.gob.dntic.arkalite.solicitudes.dominio;


/*
 * ============================================================
 *  LAB 10 (Sesión 20) — Pruebas de aceptación de la Etapa 1
 *  Programa Upskilling Backend Java · DNTIC
 * ============================================================
 *
 *  Estas pruebas verifican que su dominio de ARKA-Lite quedó bien —
 *  y, sobre todo, que se puede probar SIN LEVANTAR SPRING (ese es el
 *  criterio de aceptación de la Etapa 1). Fíjense: se arma el servicio
 *  con  new ServicioDeSolicitudes(new RepositorioEnMemoria())  —
 *  nada de @SpringBootTest, nada de servidor.
 *
 *  Colóquenlas en  src/test/java/pa/gob/dntic/solicitudes/dominio/
 *  y córranlas. Las 5 deben quedar en VERDE. No las modifiquen.
 *
 *  >>> Si un import sale en ROJO: Alt+Enter -> Import.
 * ============================================================
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa.gob.dntic.arkalite.solicitudes.adaptadores.salida.RepositorioEnMemoria;
import pa.gob.dntic.arkalite.transporte.BusDeEventosEnMemoria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PruebasDeAceptacionDominio {

    private ServicioDeSolicitudes servicio;

    @BeforeEach
    void setUp() {
        // El dominio se prueba solo: se le inyecta un adaptador en memoria, sin Spring.
        servicio = new ServicioDeSolicitudes(new RepositorioEnMemoria(), new BusDeEventosEnMemoria());
    }

    @Test
    void registrar_deja_la_solicitud_en_BORRADOR() {
        Solicitud s = servicio.registrar("INC-001", "Incidente");
        assertEquals(Estado.BORRADOR, s.estado());
    }

    @Test
    void enviar_pasa_la_solicitud_a_ENVIADA() {
        servicio.registrar("INC-001", "Incidente");
        Solicitud enviada = servicio.enviar("INC-001");
        assertEquals(Estado.ENVIADA, enviada.estado());
    }

    @Test
    void listar_devuelve_todo_lo_registrado() {
        servicio.registrar("INC-001", "Incidente");
        servicio.registrar("CAM-002", "Cambio");
        assertEquals(2, servicio.listar().size());
    }

    @Test
    void buscar_algo_que_no_existe_lanza_SolicitudNoEncontrada() {
        assertThrows(SolicitudNoEncontrada.class, () -> servicio.buscar("NADA"));
    }

    @Test
    void enviar_dos_veces_lanza_IllegalState() {
        servicio.registrar("INC-001", "Incidente");
        servicio.enviar("INC-001");                 // ya queda ENVIADA
        assertThrows(IllegalStateException.class, () -> servicio.enviar("INC-001"));
    }

    //Se agrego 3 pruebas nuevas para testear
    @Test
    void registrar_dos_solicitudes_ambos_estados_independientes() {
        servicio.registrar("INC-001", "Incidente");
        servicio.registrar("CAM-002", "Cambio");

        servicio.enviar("INC-001");

        assertEquals(Estado.ENVIADA, servicio.buscar("INC-001").estado());
        assertEquals(Estado.BORRADOR, servicio.buscar("CAM-002").estado());
    }

    @Test
    void aprobar_una_solicitud_enviada_pasa_a_APROBADA() {
        servicio.registrar("INC-001", "Incidente");
        servicio.enviar("INC-001");

        Solicitud aprobada = servicio.aprobar("INC-001");

        assertEquals(Estado.APROBADA, aprobada.estado());
    }

    @Test
    void enviar_una_solicitud_ya_aprobada_lanza_excepcion_con_mensaje() {
        servicio.registrar("INC-001", "Incidente");
        servicio.enviar("INC-001");
        servicio.aprobar("INC-001");

        IllegalStateException excepcion = assertThrows(
                IllegalStateException.class,
                () -> servicio.enviar("INC-001")
        );

        assertEquals(
                "solo se puede enviar una solicitud en BORRADOR",
                excepcion.getMessage()
        );
    }
}