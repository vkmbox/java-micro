package vkmbox.micro.lib.errors;

//import feign.FeignException;
import org.springframework.http.HttpStatus;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class ApplicationError extends RuntimeException {
    //private static final Pattern FEIGN_MESSAGE = Pattern.compile("\"message\":\"([^\"]*)\"");
    
    private final Object[] ids;
    private final ErrorType type;
    
    public Object[] getId() {
        return ids;
    }
    public ErrorType getType() {
        return type;
    }

    public ApplicationError( ErrorType type, String id) {
        this.type = type;
        this.ids = new Object[]{id};
    }

    public ApplicationError( ErrorType type, Object... ids) {
        this.type = type;
        this.ids = ids;
    }
    
    @Override
    public String getMessage() {
        return (ids == null || ids.length == 0) ? type.getMessagePattern() : 
          String.format(type.getMessagePattern(), ids);
    }
    
    public HttpStatus getStatus() {
        return type.getStatus();
    }

    /*public ApplicationError( FeignException cause ) {
        String message = cause.getMessage();
        Matcher matcher = FEIGN_MESSAGE.matcher(message);
        if ( matcher.find() ) {
            message = matcher.group(1);
        }
        HttpStatus status;
        try {
            status = HttpStatus.valueOf(cause.status());
        } catch ( Exception ex ) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        this.type = new ErrorType(message, status);
        this.ids = null;
    }*/
    
}
