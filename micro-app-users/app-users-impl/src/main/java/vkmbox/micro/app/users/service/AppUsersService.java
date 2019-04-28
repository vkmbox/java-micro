package vkmbox.micro.app.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.sys.keycloak.controller.SysKeycloakInterface;

@Service
public class AppUsersService
{
    private final SysKeycloakInterface keycloak;
    
    @Autowired
    public AppUsersService(SysKeycloakInterface keycloak){
        this.keycloak = keycloak;
    }

    public UserDto registerWithUsername(UserPswDto registerDto)
    {
        UserDto dto = null;
        ResponseEntity<UserDto> response = keycloak.addUser(dto);
        if ( HttpStatus.CREATED != response.getStatusCode() ) {
            //TODO
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not created");
        }
        return response.getBody();
    }
}
