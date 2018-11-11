package vkmbox.micro.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import vkmbox.micro.user.dto.CredentialDto;
import vkmbox.micro.user.dto.UserDto;

import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import vkmbox.micro.user.service.UserService;

@Slf4j
@RestController
public class UsersController {
    
    //@Inject
    //PortListener config;
    
    //@Inject
    //Environment environment;
  
    //@Value("${keycloak.auth-server-url}")
    private String authServerUrl = "http://localhost:8180/auth/";

    //@Value("${keycloak.realm}")
    private String realm = "micro";

    //@Value("${keycloak.resource}")
    private String user = "manager";

    private String password = "password";
    
    String clientId = "micro-cli";
    String clientSecret = "92acc719-4496-4bcf-95c4-4ac06d05490d";
    private UserService userService;
    
    @Autowired
    public UsersController( UserService userService ) {
      this.userService = userService;
    }
    
    @GetMapping("/all")
    public List<UserRepresentation> getUsers() {
      Keycloak instance = getInstance(); //.get("31bf198e-9f55-4e9e-996b-0bc79acf81fc")
      return instance.realm(realm).users().list(); //.toRepresentation(); //.users().list();
    }
    
    @PostMapping("/add")
    public Response addUser( @RequestBody UserDto userDto ) {
      Keycloak instance = getInstance(); //.get("31bf198e-9f55-4e9e-996b-0bc79acf81fc")
      return instance.realm(realm).users().create(userService.getUserRepresentation(userDto));
    }
    
    @GetMapping("/test")
    public String getTest() {
      return "Ok";
    }
    
    private Keycloak getInstance() {
      return Keycloak.getInstance(authServerUrl, realm, user, password, clientId, clientSecret); //, "security-admin-console"
    }
    
}
