package pa.gob.dntic.servicionotificaciones.adaptadores.salida;

import org.junit.jupiter.api.Test;
import pa.gob.dntic.servicionotificaciones.dominio.Notificacion;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RepositorioEnMemoriaTest {

    @Test
    void conservaElOrdenYExponeUnaVistaInmutable() {
        RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
        repositorio.guardar(new Notificacion("Primera"));
        repositorio.guardar(new Notificacion("Segunda"));

        List<Notificacion> notificaciones = repositorio.todas();

        assertEquals(List.of(new Notificacion("Primera"), new Notificacion("Segunda")), notificaciones);
        assertThrows(UnsupportedOperationException.class,
                () -> notificaciones.add(new Notificacion("Tercera")));
    }
}
