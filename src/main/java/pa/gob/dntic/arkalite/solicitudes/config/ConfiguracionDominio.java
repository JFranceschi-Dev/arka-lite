package pa.gob.dntic.arkalite.solicitudes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pa.gob.dntic.arkalite.solicitudes.dominio.PublicadorDeEventos;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.ServicioDeSolicitudes;

@Configuration
public class ConfiguracionDominio {

    @Bean
    public ServicioDeSolicitudes servicioDeSolicitudes(RepositorioDeSolicitudes repositorio,
                                                       PublicadorDeEventos publicador) {
        return new ServicioDeSolicitudes(repositorio, publicador);
    }

    @Bean
    public CommandLineRunner datosDeEjemplo(ServicioDeSolicitudes servicio) {
        return args -> {
            servicio.registrar("INC-001", "Incidente");
            servicio.registrar("CAM-002", "Cambio");
            servicio.enviar("CAM-002");   // dispara una notificación al arrancar
        };
    }
}
