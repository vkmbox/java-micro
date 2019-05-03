package vkmbox.micro.sys.keycloak.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import vkmbox.micro.lib.errors.ApplicationControllerAdvice;

@ControllerAdvice(annotations = RestController.class)
public class UsersControllerAdvice extends ApplicationControllerAdvice
{
}
