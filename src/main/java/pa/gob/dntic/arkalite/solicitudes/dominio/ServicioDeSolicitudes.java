package pa.gob.dntic.arkalite.solicitudes.dominio;

import pa.gob.dntic.arkalite.eventos.SolicitudEnviada;
import java.util.List;

/*
 * Dominio de Solicitudes (Etapa 1) + publicación de evento (Etapa 2).
 * Depende de dos PUERTOS: el repositorio y el publicador. NO conoce a Notificaciones.
 */
public class ServicioDeSolicitudes {

    private final RepositorioDeSolicitudes repositorio;
    private final PublicadorDeEventos publicador;

    public ServicioDeSolicitudes(RepositorioDeSolicitudes repositorio, PublicadorDeEventos publicador) {
        this.repositorio = repositorio;
        this.publicador = publicador;
    }

    public Solicitud registrar(String id, String tipo) {
        Solicitud s = new Solicitud(id, tipo, Estado.BORRADOR);
        repositorio.guardar(s);
        return s;
    }

    public Solicitud enviar(String id) {
        Solicitud enviada = buscar(id).enviar();
        repositorio.guardar(enviada);
        publicador.publicar(new SolicitudEnviada(enviada.id(), enviada.tipo()));  // avisa; no sabe quién reacciona
        return enviada;
    }

    public List<Solicitud> listar() { return repositorio.todas(); }

    public Solicitud buscar(String id) {
        return repositorio.buscar(id).orElseThrow(() -> new SolicitudNoEncontrada(id));
    }
}
