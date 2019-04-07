package vkmbox.micro.sys.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.CredentialDto;
import vkmbox.micro.sys.keycloak.errors.UserError;
import vkmbox.micro.sys.keycloak.errors.UserError.ErrorType;
import org.springframework.http.ResponseEntity;
import vkmbox.micro.sys.keycloak.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UsersController implements SysKeycloakInterface {
    
    private final UserService userService;

    @Autowired
    public UsersController( UserService userService ) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers().stream()
          .map( value -> userService.getUserDtoFromRepresentationNoCredentials(value))
          .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
      return Optional.of(userService.getUserDtoFromRepresentationNoCredentials(userService.getUserById(id)))
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new UserError(ErrorType.USER_NOT_FOUND, id));
    }

    @Override
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        String id = userService.addUser( userDto );
        UserDto dto = userService.getUserDtoFromRepresentationNoCredentials(userService.getUserById(id));
        URI uri = MvcUriComponentsBuilder.fromController(getClass())
          .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Override
    public ResponseEntity<?> resetPassword( @PathVariable String userid
      , @RequestBody CredentialDto credentialDto ) {
        return ResponseEntity.ok(userService.resetPassword(userid, credentialDto));
    }

    @Override
    public ResponseEntity<?> deleteUser( @PathVariable String id )
    {
      return ResponseEntity.ok(userService.deleteUser(id));
    }
}
