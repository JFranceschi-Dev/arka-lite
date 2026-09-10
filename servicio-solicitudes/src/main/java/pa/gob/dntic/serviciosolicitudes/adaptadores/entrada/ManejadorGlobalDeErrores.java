package pa.gob.dntic.serviciosolicitudes.adaptadores.entrada;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pa.gob.dntic.serviciosolicitudes.dominio.SolicitudNoEncontrada;

@RestControllerAdvice
public class ManejadorGlobalDeErrores {

    @ExceptionHandler(SolicitudNoEncontrada.class)
    public ResponseEntity<ErrorRespuesta> noEncontrada(SolicitudNoEncontrada ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRespuesta(404, ex.getMessage()));
    }
}