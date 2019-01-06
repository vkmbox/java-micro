package vkmbox.micro.sys.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import vkmbox.micro.sys.keycloak.dto.UserDto;
import vkmbox.micro.sys.keycloak.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@RestController
public class UsersController implements UserControllerInterface {
    
    private UserService userService;
    
    @Autowired
    public UsersController( UserService userService ) {
      this.userService = userService;
    }
    
    public List<UserRepresentation> getUsers() {
        return userService.getUsers();
    }
    
    public String addUser( @RequestBody UserDto userDto ) {
      return userService.addUser( userDto );
    }
    
    @GetMapping("/test")
    public String getTest() {
      return "Ok";
    }
    
}
