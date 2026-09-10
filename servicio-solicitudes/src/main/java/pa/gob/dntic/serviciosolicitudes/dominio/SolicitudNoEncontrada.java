package pa.gob.dntic.serviciosolicitudes.dominio;

public class SolicitudNoEncontrada extends RuntimeException {
    public SolicitudNoEncontrada(String id) { super("No existe la solicitud " + id); }
}