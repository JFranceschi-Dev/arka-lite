package pa.gob.dntic.servicionotificaciones.dominio;

public class NotificacionNoEncontrada extends RuntimeException {
    public NotificacionNoEncontrada(String id) {
        super("No Existe la solicitud con el id: " + id);
    }
}
