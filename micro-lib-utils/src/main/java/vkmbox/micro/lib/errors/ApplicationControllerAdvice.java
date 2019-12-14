package vkmbox.micro.lib.errors;

import static vkmbox.micro.lib.utils.Lang.notEmpty;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.util.Optional;
import java.io.IOException;

public class ApplicationControllerAdvice
{
    private final ObjectMapper mapper = new ObjectMapper();
    private final MediaType vndErrorMediaType = 
        MediaType.parseMediaType("application/vnd.error+json");
   
    @ExceptionHandler(ApplicationError.class)
    public ResponseEntity<VndErrors> applicationError( ApplicationError ex ) {
        return error(ex, ex.getStatus(), ex.getClass().getSimpleName());
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<VndErrors> httpStatusCodeException( HttpStatusCodeException ex ) {
        String logref = ex.getClass().getSimpleName();
        VndErrors vnd = tryGetVndErrors(ex.getResponseBodyAsString());
        if (vnd != null) {
            return error(vnd, ex.getResponseHeaders(), ex.getStatusCode());
        } else {
            return error(ex.getStatusCode(), ex.getResponseHeaders()
                , ex.getResponseBodyAsString(), logref);
        }
    }

    @ExceptionHandler({UnknownHttpStatusCodeException.class})
    public ResponseEntity<VndErrors> applicationError( UnknownHttpStatusCodeException ex ) {
        String logref = ex.getClass().getSimpleName();
        String message = Optional.of(ex.getResponseBodyAsString()).orElse(logref);
        HttpHeaders headers = Optional.of(ex.getResponseHeaders()).orElseGet(()-> new HttpHeaders());
        headers.setContentType(vndErrorMediaType);
        VndErrors vnd = tryGetVndErrors(message);
        return ResponseEntity.status(ex.getRawStatusCode()).headers(headers)
            .body(vnd != null? vnd : new VndErrors(logref, message));
    }

    private <E extends Exception> ResponseEntity<VndErrors> error(VndErrors errors
            , HttpHeaders headers, HttpStatus status) {
        headers = Optional.of(headers).orElseGet(()-> new HttpHeaders());
        headers.setContentType(vndErrorMediaType);
        return new ResponseEntity<>(errors, headers, status);
    }
    
    private <E extends Exception> ResponseEntity<VndErrors> error(HttpStatus status
            , HttpHeaders headers, String message, String logref) {
        return error(new VndErrors(logref, notEmpty(message, status.getReasonPhrase())), headers, status);
    }
    
    private <E extends Exception> ResponseEntity<VndErrors> error(E error
            , HttpStatus status, String logref) {
        String message = Optional.of(error.getLocalizedMessage()).orElse(error.getClass().getSimpleName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vndErrorMediaType);
        return error(status, headers, message, logref);
    }

    private VndErrors tryGetVndErrors(String responseBodyAsString) {
        try {
            return mapper.readValue(responseBodyAsString, VndErrors.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
