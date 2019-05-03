package vkmbox.micro.app.users.service;

import static vkmbox.micro.app.users.errors.AppUsersControllerAdvice.APP_USERS_USER_NOT_CREATED;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.lib.dto.CredentialDto;
import vkmbox.micro.lib.dto.CredentialDto.Type;
import vkmbox.micro.sys.keycloak.controller.SysKeycloakInterface;
import vkmbox.micro.lib.errors.ApplicationError;

import java.util.List;

@Service
public class AppUsersService
{
    private final SysKeycloakInterface keycloak;
    
    @Autowired
    public AppUsersService(SysKeycloakInterface keycloak){
        this.keycloak = keycloak;
    }

    public UserDto registerWithUsername(UserPswDto psw)
    {
        CredentialDto credentials = new CredentialDto().setType(Type.password)
            .setValue(new String(psw.getPassword())).setTemporary(false);
        UserDto dto = new UserDto().setUsername(psw.getUser())
            .setCredentials(List.of(credentials)).setEnabled(true)
            .setFirstName(psw.getFirstName()).setLastName(psw.getLastName())
            .setEmail(psw.getEmail());
        ResponseEntity<UserDto> response;
        try {
            response = keycloak.addUser(dto);
        } catch (FeignException ex) {
            throw new ApplicationError(ex);
        }
        if ( HttpStatus.CREATED != response.getStatusCode() ) {
            throw new ApplicationError(APP_USERS_USER_NOT_CREATED, response.getStatusCode().value());
        }
        return response.getBody();
    }
}
