package vkmbox.micro.app.users.service;

import static vkmbox.micro.app.users.errors.AppUsersControllerAdvice.NO_TOKEN_FOR_USER;
import static vkmbox.micro.app.users.errors.AppUsersControllerAdvice.APP_USERS_USER_NOT_CREATED;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.TokenDto;
import vkmbox.micro.lib.dto.CredentialDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.lib.dto.CredentialDto.Type;
import vkmbox.micro.lib.errors.ApplicationError;
import vkmbox.micro.app.users.dto.ResetPasswordDto;
import vkmbox.micro.sys.keycloak.controller.SysKeycloakInterface;

import java.util.List;
import java.util.Base64;

@Service
public class AppUsersService
{
    private final SysKeycloakInterface keycloak;
    
    @Autowired
    public AppUsersService(SysKeycloakInterface keycloak) {
        this.keycloak = keycloak;
    }

    public UserDto registerWithUsername(UserPswDto psw) {
        UserDto dto = getUserFromUserPsw(psw);
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
    
    public TokenDto getToken(UserPswDto psw) {
        UserDto dto = getUserFromUserPsw(psw);
        ResponseEntity<TokenDto> response;
        try {
            response = keycloak.getToken(dto);
        } catch (FeignException ex) {
            throw new ApplicationError(ex);
        }
        if ( HttpStatus.OK != response.getStatusCode() ) {
            throw new ApplicationError(NO_TOKEN_FOR_USER, response.getStatusCode().value());
        }
        return response.getBody();
    }
    
    public UserDto deleteUser(String id) {
        UserDto dto = getUser(id);
        keycloak.deleteUser(id);
        return dto;
    }
    
    public UserDto getUser(String id) {
        ResponseEntity<UserDto> user = keycloak.getUser(id);
        return user.getBody();
    }
    
    public UserDto resetPassword(String id, ResetPasswordDto resetDto) {
        UserDto dto = getUser(id);
        UserPswDto psw = new UserPswDto()
            .setUser(dto.getUsername()).setPassword(resetDto.getOldPassword());
        getToken(psw);
        CredentialDto credentialDto = new CredentialDto()
            .setType(Type.password).setTemporary(Boolean.FALSE)
            .setValue(getBase64FromString(resetDto.getNewPassword()));
        keycloak.resetPassword(id, credentialDto);
        return dto;
    }
    
    private UserDto getUserFromUserPsw(UserPswDto psw) {
        CredentialDto credentials = new CredentialDto().setType(Type.password)
            .setValue(getBase64FromString(psw.getPassword()))
            .setTemporary(false);
        return new UserDto().setUsername(psw.getUser())
            .setCredentials(List.of(credentials)).setEnabled(true)
            .setFirstName(psw.getFirstName()).setLastName(psw.getLastName())
            .setEmail(psw.getEmail());
    }
    
    private String getBase64FromString(byte[] value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
