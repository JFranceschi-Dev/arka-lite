package pa.gob.dntic.arkalite.transporte;

import org.springframework.stereotype.Component;
import pa.gob.dntic.arkalite.eventos.SolicitudEnviada;
import pa.gob.dntic.arkalite.solicitudes.dominio.PublicadorDeEventos;
import java.util.*;
import java.util.function.Consumer;

/*
 * ADAPTADOR de transporte. Cumple el puerto PublicadorDeEventos.
 * Hoy en memoria; en la Etapa 4 será REST o RabbitMQ, cambiando SOLO esta clase.
 * Un consumidor que falla NO tumba al publicador (desacople real).
 */
@Component
public class BusDeEventosEnMemoria implements PublicadorDeEventos {
    private final List<Consumer<SolicitudEnviada>> suscriptores = new ArrayList<>();

    public void suscribir(Consumer<SolicitudEnviada> manejador) {
        suscriptores.add(manejador);
    }

    public void publicar(SolicitudEnviada evento) {
        for (Consumer<SolicitudEnviada> s : suscriptores) {
            try {
                s.accept(evento);
            }
            catch (RuntimeException fallo) {
                System.err.println("consumidor falló: " + fallo.getMessage());
            }
        }
    }
}
