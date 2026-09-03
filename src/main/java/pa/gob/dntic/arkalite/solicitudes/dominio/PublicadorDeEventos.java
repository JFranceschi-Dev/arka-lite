package pa.gob.dntic.arkalite.solicitudes.dominio;

import pa.gob.dntic.arkalite.eventos.SolicitudEnviada;

/* PUERTO de salida de Solicitudes: "avisar al mundo" sin saber quién escucha. */
public interface PublicadorDeEventos {
    void publicar(SolicitudEnviada evento);
}
