package vkmbox.micro.lib.errors;

import org.springframework.http.HttpStatus;

public class ErrorType {
    
    public static final ErrorType INTERNAL_SERVER_ERROR = new ErrorType("%s", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorType USER_ALREADY_EXISTS = new ErrorType("User with username=%s already exists", HttpStatus.CONFLICT);
    public static final ErrorType USER_NOT_CREATED = new ErrorType("User has not been created", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorType USER_NOT_FOUND = new ErrorType("User with id=%s not exists", HttpStatus.NOT_FOUND);
    public static final ErrorType DELETING_USER_MANAGER_PROHIBITED = new ErrorType("Deleting the user manager is prohibited", HttpStatus.FORBIDDEN);
        
    private final String messagePattern;
    private final HttpStatus status;

    public final String getMessagePattern() {
        return messagePattern;
    }
    public final HttpStatus getStatus() {
        return status;
    }

    public ErrorType (String messagePattern, HttpStatus status) {
        this.messagePattern = messagePattern;
        this.status = status;
    }
    
}
