package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pa.gob.dntic.servicionotificaciones.adaptadores.salida.RepositorioEnMemoria;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificacionControllerWebTest {

    private MockMvc mvc;

    @BeforeEach
    void prepararMvc() {
        ServicioDeNotificaciones servicio = new ServicioDeNotificaciones(new RepositorioEnMemoria());
        mvc = MockMvcBuilders.standaloneSetup(
                new EventoController(servicio), new NotificacionController(servicio)
        ).build();
    }

    @Test
    void recibeUnEventoYExponeLaNotificacionComoJson() throws Exception {
        mvc.perform(post("/eventos/solicitud-enviada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"SOL-001\",\"tipo\":\"Incidencia\"}"))
                .andExpect(status().isOk());

        mvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Solicitud SOL-001 (Incidencia) enviada"));
    }
}
