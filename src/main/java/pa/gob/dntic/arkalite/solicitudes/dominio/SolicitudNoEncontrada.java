package pa.gob.dntic.arkalite.solicitudes.dominio;

public class SolicitudNoEncontrada extends RuntimeException {
    public SolicitudNoEncontrada(String id) {

        super("No Existe la solicitud con el id: " + id);
    }
}
