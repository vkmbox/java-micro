package vkmbox.micro.sys.keycloak.controller;

import lombok.extern.slf4j.Slf4j;
import vkmbox.micro.sys.keycloak.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
//      Keycloak instance = getInstance(); //.get("31bf198e-9f55-4e9e-996b-0bc79acf81fc")
//      return instance.realm(realm).users().list(); //.toRepresentation(); //.users().list();
    }
    
    //@PostMapping("/add")
    //public String addUser( @RequestBody UserDto userDto ) {
//      Keycloak instance = getInstance(); //.get("31bf198e-9f55-4e9e-996b-0bc79acf81fc")
//      return instance.realm(realm).users().create(userService.getUserRepresentation(userDto));
    //}
    
    @GetMapping("/test")
    public String getTest() {
      return "Ok";
    }
    
    /*private Keycloak getInstance() {
      return Keycloak.getInstance(authServerUrl, realm, user, password, clientId, clientSecret); //, "security-admin-console"
    }*/
    
}
