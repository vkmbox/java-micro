package vkmbox.micro.lib.errors;

import feign.FeignException;
import org.springframework.http.HttpStatus;

public class ApplicationError extends RuntimeException
{
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

    //TODO: get message field form text:
    //status 409 reading SysKeycloakInterface#addUser(UserDto); content:
    //[{"logref":"manager4","message":"User with username=manager4 already exists","links":[]}]
    public ApplicationError( FeignException cause ) {
        this.type = new ErrorType(cause.getMessage(), HttpStatus.valueOf(cause.status()));
        this.ids = null;
    }
    
}
