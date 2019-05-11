package vkmbox.micro.app.users.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import vkmbox.micro.lib.errors.ApplicationControllerAdvice;
import vkmbox.micro.lib.errors.ErrorType;

@ControllerAdvice(annotations = RestController.class)
public class AppUsersControllerAdvice extends ApplicationControllerAdvice {
    public static final ErrorType APP_USERS_USER_NOT_CREATED 
        = new ErrorType("User not created, http response code: %d", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorType NO_TOKEN_FOR_USER
        = new ErrorType("No token for user, http response code: %d", HttpStatus.INTERNAL_SERVER_ERROR);
}
