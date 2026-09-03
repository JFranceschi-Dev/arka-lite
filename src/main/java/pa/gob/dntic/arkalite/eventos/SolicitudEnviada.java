package pa.gob.dntic.arkalite.eventos;
/* El contrato compartido entre servicios. Lo conocen ambos; nadie conoce al otro servicio. */
public record SolicitudEnviada(String id, String tipo) {}
