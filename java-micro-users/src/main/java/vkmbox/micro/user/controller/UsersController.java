package vkmbox.micro.user.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/all")
    public List<UserRepresentation> getUsers() {
      Keycloak instance = getInstance(); //.get("31bf198e-9f55-4e9e-996b-0bc79acf81fc")
      return instance.realm("micro").users().list(); //.toRepresentation(); //.users().list();
    }
    
    @GetMapping("/test")
    public String getTest() {
      return "Ok";
    }
    
    private Keycloak getInstance() {
      return Keycloak.getInstance(authServerUrl, realm, user, password, clientId, clientSecret); //, "security-admin-console"
    }
    
}
