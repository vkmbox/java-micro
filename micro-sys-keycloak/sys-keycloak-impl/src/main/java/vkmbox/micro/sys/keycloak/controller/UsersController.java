package vkmbox.micro.sys.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.CredentialDto;
import vkmbox.micro.sys.keycloak.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UsersController implements SysKeycloakInterface {
    
  private final UserService userService;

  @Autowired
  public UsersController( UserService userService ) {
    this.userService = userService;
  }

  @Override
  public List<UserRepresentation> getUsers() {
    return userService.getUsers();
  }

  @Override
  public String addUser( @RequestBody UserDto userDto ) {
    return userService.addUser( userDto );
  }

  @Override
  public String resetPassword ( @PathVariable("uuid") String uuid
    , @RequestBody CredentialDto credentialDto ) {
    return userService.resetPassword( uuid, credentialDto );
  }

  @Override
  public String deleteUser( @PathVariable("uuid") String uuid )
  {
    return userService.deleteUser( uuid );
  }
}
