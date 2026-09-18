package pa.gob.dntic.servicionotificaciones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pa.gob.dntic.servicionotificaciones.adaptadores.entrada.ManejadorDeEventos;
import pa.gob.dntic.servicionotificaciones.adaptadores.entrada.NotificacionController;
import pa.gob.dntic.servicionotificaciones.adaptadores.salida.RepositorioEnMemoria;
import pa.gob.dntic.servicionotificaciones.dominio.Notificacion;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicioNotificacionesApplicationTests {

    private ManejadorDeEventos manejador;
    private NotificacionController controller;

    @BeforeEach
    void onBefore() {
        ServicioDeNotificaciones servicio = new ServicioDeNotificaciones(new RepositorioEnMemoria());
        manejador = new ManejadorDeEventos(servicio);
        controller = new NotificacionController(servicio);
    }

    @Test
    void givenUnEventoCuandoSeManeja_thenSeGeneraLaNotificacion() {
        manejador.manejar(new SolicitudEnviada("SOL-100", "Incidencia"));

        List<Notificacion> notificaciones = controller.todas();

        assertEquals(1, notificaciones.size());
        assertEquals("Solicitud SOL-100 (Incidencia) enviada", notificaciones.getFirst().texto());
    }

    @Test
    void givenDosEventosCuandoSeReciben_thenSeListanLasDosNotificaciones() {
        manejador.manejar(new SolicitudEnviada("SOL-101", "Cambio"));
        manejador.manejar(new SolicitudEnviada("SOL-102", "Reclamo"));

        List<Notificacion> notificaciones = controller.todas();

        assertEquals(2, notificaciones.size());
        assertEquals("Solicitud SOL-101 (Cambio) enviada", notificaciones.get(0).texto());
        assertEquals("Solicitud SOL-102 (Reclamo) enviada", notificaciones.get(1).texto());
    }

    @Test
    void givenListaVacia_whenSeConsultaNotificaciones_thenDevuelveListaVacia() {
        List<Notificacion> notificaciones = controller.todas();

        assertEquals(0, notificaciones.size());
    }
}

