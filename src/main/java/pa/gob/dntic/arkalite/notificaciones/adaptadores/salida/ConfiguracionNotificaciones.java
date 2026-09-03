package pa.gob.dntic.arkalite.notificaciones.adaptadores.salida;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pa.gob.dntic.arkalite.notificaciones.dominio.RepositorioDeNotificaciones;
import pa.gob.dntic.arkalite.notificaciones.dominio.ServicioDeNotificaciones;

@Configuration
public class ConfiguracionNotificaciones {
    @Bean
    public ServicioDeNotificaciones servicioDeNotificaciones(RepositorioDeNotificaciones repositorio) {
        return new ServicioDeNotificaciones(repositorio);
    }
}
