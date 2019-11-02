package vkmbox.micro.lib.errors;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;
import org.springframework.web.client.UnknownHttpStatusCodeException;

public class ApplicationControllerAdvice
{
    private final MediaType vndErrorMediaType = 
        MediaType.parseMediaType("application/vnd.error+json");
   
    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<VndErrors> applicationError( ApplicationError ex ) {
        return error(ex, ex.getStatus(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<VndErrors> httpStatusCodeException( HttpStatusCodeException ex ) {
        return error(ex, ex.getStatusCode(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler({UnknownHttpStatusCodeException.class})
    public ResponseEntity<VndErrors> applicationError( UnknownHttpStatusCodeException ex ) {
        String logref = ex.getClass().getSimpleName();
        String message = Optional.of(ex.getLocalizedMessage()).orElse(logref);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vndErrorMediaType);
        return ResponseEntity.status(ex.getRawStatusCode()).headers(headers)
            .body(new VndErrors(logref, message));
    }
    
    private <E extends Exception> ResponseEntity<VndErrors> error(E error
            , HttpStatus status, String logref) {
        String message = Optional.of(error.getLocalizedMessage()).orElse(error.getClass().getSimpleName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vndErrorMediaType);
        return new ResponseEntity<>(new VndErrors(logref, message), headers, status);
    }
}
