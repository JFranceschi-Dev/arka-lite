package pa.gob.dntic.servicionotificaciones.adaptadores.salida;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pa.gob.dntic.servicionotificaciones.dominio.RepositorioDeNotificaciones;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;

@Configuration
public class ConfiguracionNotificaciones {
    @Bean
    public ServicioDeNotificaciones servicioDeNotificaciones(RepositorioDeNotificaciones repositorio) {
        return new ServicioDeNotificaciones(repositorio);
    }
}
