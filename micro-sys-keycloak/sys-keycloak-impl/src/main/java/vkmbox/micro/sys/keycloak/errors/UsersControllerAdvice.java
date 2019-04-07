package vkmbox.micro.sys.keycloak.errors;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@ControllerAdvice(annotations = RestController.class)
public class UsersControllerAdvice
{
    private final MediaType vndErrorMediaType = 
        MediaType.parseMediaType("application/vnd.error+json"); //("application/vnd.error");
   
    @ExceptionHandler(UserError.class)
    public ResponseEntity<VndErrors> userException( UserError ex ) {
        return error(ex, ex.getStatus(), "" + ex.getId());
    }
   
    private <E extends Exception> ResponseEntity<VndErrors> error(E error
      , HttpStatus status, String logref) {
        String message = Optional.of(error.getMessage()).orElse(error.getClass().getSimpleName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vndErrorMediaType);
        return new ResponseEntity<>(new VndErrors(logref, message), headers, status);
    }
}
