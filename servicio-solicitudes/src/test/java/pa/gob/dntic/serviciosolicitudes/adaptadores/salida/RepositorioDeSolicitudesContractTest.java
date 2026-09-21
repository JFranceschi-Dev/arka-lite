package pa.gob.dntic.serviciosolicitudes.adaptadores.salida;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pa.gob.dntic.serviciosolicitudes.dominio.Estado;
import pa.gob.dntic.serviciosolicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.serviciosolicitudes.dominio.Solicitud;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RepositorioDeSolicitudesContractTest {

    static Stream<RepositorioDeSolicitudes> repositorios() {
        return Stream.of(new RepositorioEnMemoria(), new RepositorioQueRegistra());
    }

    @ParameterizedTest
    @MethodSource("repositorios")
    void guardaActualizaYBuscaPorIdentificador(RepositorioDeSolicitudes repositorio) {
        repositorio.guardar(new Solicitud("SOL-001", "Incidencia", Estado.BORRADOR));
        repositorio.guardar(new Solicitud("SOL-001", "Incidencia", Estado.ENVIADA));

        assertEquals(1, repositorio.todas().size());
        assertEquals(Estado.ENVIADA, repositorio.buscar("SOL-001").orElseThrow().estado());
        assertFalse(repositorio.buscar("NO-EXISTE").isPresent());
    }
}
