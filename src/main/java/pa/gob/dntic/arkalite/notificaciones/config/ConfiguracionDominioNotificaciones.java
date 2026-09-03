package pa.gob.dntic.arkalite.notificaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pa.gob.dntic.arkalite.notificaciones.dominio.RepositorioDeNotificacion;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;

@Configuration
public class ConfiguracionDominioNotificaciones {

    @Bean
    // Bean es un fragmento de codigo donde  se puede usar globalmente. en este caso es para poder cambiar el repositorio
    public ServicioDeNotificaciones servicioDeNotificaciones(RepositorioDeNotificacion repositorio){
        return new ServicioDeNotificaciones(repositorio);
    }
}