package vkmbox.micro.lib.errors;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

public class ApplicationControllerAdvice
{
    private final MediaType vndErrorMediaType = 
        MediaType.parseMediaType("application/vnd.error+json"); //("application/vnd.error");
   
    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<VndErrors> applicationError( ApplicationError ex ) {
        return error(ex, ex.getStatus(), ex.getClass().getSimpleName());
    }
   
    private <E extends Exception> ResponseEntity<VndErrors> error(E error
      , HttpStatus status, String logref) {
        String message = Optional.of(error.getMessage()).orElse(error.getClass().getSimpleName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vndErrorMediaType);
        return new ResponseEntity<>(new VndErrors(logref, message), headers, status);
    }
}
