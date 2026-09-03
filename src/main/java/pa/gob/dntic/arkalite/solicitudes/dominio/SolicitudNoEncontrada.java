package pa.gob.dntic.arkalite.solicitudes.dominio;

public class SolicitudNoEncontrada extends RuntimeException {
    public SolicitudNoEncontrada(String id) { super("No existe la solicitud " + id); }
}