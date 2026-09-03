package pa.gob.dntic.arkalite.solicitudes.adaptadores.entrada;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pa.gob.dntic.arkalite.solicitudes.dominio.SolicitudNoEncontrada;


@RestControllerAdvice
public class ManejadorGlobalDeErrores {

    @ExceptionHandler(SolicitudNoEncontrada.class)
    public ResponseEntity<pa.gob.dntic.arkalite.solicitudes.adaptadores.entrada.ErrorRespuesta> noEncontrada(SolicitudNoEncontrada ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRespuesta(404,ex.getMessage()));
    }

}
