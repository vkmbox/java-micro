package vkmbox.micro.sys.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import vkmbox.micro.sys.keycloak.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vkmbox.micro.sys.keycloak.dto.UserDto;

//TODO: Extract interface using feign client annotation
@Slf4j
@RestController
public class UsersController {
    
    //@Inject
    //PortListener config;
    
    //@Inject
    //Environment environment;
  
    private UserService userService;
    
    @Autowired
    public UsersController( UserService userService ) {
      this.userService = userService;
    }
    
    @GetMapping("/all")
    public List<UserRepresentation> getUsers() {
        return userService.getUsers();
    }
    
    @PostMapping("/add")
    public String addUser( @RequestBody UserDto userDto ) {
      return userService.addUser( userDto );
    }
    
    @GetMapping("/test")
    public String getTest() {
      return "Ok";
    }
    
}
