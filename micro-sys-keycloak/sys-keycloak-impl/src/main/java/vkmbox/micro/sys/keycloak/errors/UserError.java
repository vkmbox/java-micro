package vkmbox.micro.sys.keycloak.errors;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class UserError extends RuntimeException
{
    public enum ErrorType {
        USER_ALREADY_EXISTS("User with username=%s already exists", HttpStatus.CONFLICT),
        USER_NOT_CREATED("User has not been created", HttpStatus.INTERNAL_SERVER_ERROR),
        USER_NOT_FOUND("User with id=%s not exists", HttpStatus.NOT_FOUND),
        DELETING_USER_MANAGER_PROHIBITED("Deleting the user manager is prohibited", HttpStatus.FORBIDDEN);
        
        private final String messagePattern;
        private final HttpStatus status;
        
        public final String getMessagePattern() {
            return messagePattern;
        }
        public final HttpStatus getStatus() {
            return status;
        }
        
        ErrorType (String messagePattern, HttpStatus status) {
            this.messagePattern = messagePattern;
            this.status = status;
        }
    }
    
    private final String id;
    private final ErrorType type;
    
    public String getId() {
        return id;
    }
    public ErrorType getType() {
        return type;
    }

    public UserError( ErrorType type ) {
        this(type, "");
    }
    
    public UserError( ErrorType type, String id) {
        this.type = type;
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return StringUtils.isEmpty(id) ? type.getMessagePattern() : 
          String.format(type.getMessagePattern(), id);
    }
    
    public HttpStatus getStatus() {
        return type.getStatus();
    }
    
}
