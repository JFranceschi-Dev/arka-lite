package pa.gob.dntic.arkalite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pa.gob.dntic.arkalite.notificaciones.adaptadores.entrada.ManejadorDeEventos;
import pa.gob.dntic.arkalite.transporte.BusDeEventosEnMemoria;

/*
 * Raíz de composición de ARKA-Lite. Vive en pa.gob.dntic.arkalite para que el escaneo
 * cubra AMBOS módulos (solicitudes y notificaciones).
 */
@SpringBootApplication
public class ArkaliteApplication {
    public static void main(String[] args) { SpringApplication.run(ArkaliteApplication.class, args); }

    // Enchufa el manejador de notificaciones al bus: aquí se conectan los dos servicios.
    @Bean
    CommandLineRunner suscribirNotificaciones(BusDeEventosEnMemoria bus, ManejadorDeEventos manejador) {
        return args -> bus.suscribir(manejador::manejar);
    }
}
