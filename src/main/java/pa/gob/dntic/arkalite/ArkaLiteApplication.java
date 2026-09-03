package pa.gob.dntic.arkalite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArkaLiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArkaLiteApplication.class, args);
    }

    // Enchufa el manejador de notificaciones al bus: aquí se conectan los dos servicios.
//    @Bean
//    CommandLineRunner suscribirNotificaciones(BusDeEventosEnMemoria bus, ManejadorDeEventos manejador) {
//        return args -> bus.suscribir(manejador::manejar);
//    }
}
