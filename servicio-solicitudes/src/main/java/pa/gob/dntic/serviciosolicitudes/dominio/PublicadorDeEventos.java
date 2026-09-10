package pa.gob.dntic.serviciosolicitudes.dominio;

import pa.gob.dntic.serviciosolicitudes.eventos.SolicitudEnviada;

/* PUERTO de salida de Solicitudes: "avisar al mundo" sin saber quién escucha. */
public interface PublicadorDeEventos {
    void publicar(SolicitudEnviada evento);
}
