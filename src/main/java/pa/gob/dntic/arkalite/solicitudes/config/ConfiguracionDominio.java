package pa.gob.dntic.arkalite.solicitudes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pa.gob.dntic.arkalite.solicitudes.dominio.RepositorioDeSolicitudes;
import pa.gob.dntic.arkalite.solicitudes.dominio.ServicioDeSolicitudes;

@Configuration
public class ConfiguracionDominio
{

    @Bean // Bean es un fragmento de codigo donde  se puede usar globalmente. en este caso es para poder cambiar el repositorio
    public ServicioDeSolicitudes servicioDeSolicitudes(RepositorioDeSolicitudes repositorio){
        return new ServicioDeSolicitudes(repositorio);
    }

    @Bean
    public CommandLineRunner datosEjemplo(ServicioDeSolicitudes servicio){
        return args -> {
            servicio.registrar("INEC-001","Incidente");
            servicio.registrar("INEC-002","Cambio");
//            servicio.enviar("CAM-002");
            servicio.enviar("INEC-002");
        };
    }
}
