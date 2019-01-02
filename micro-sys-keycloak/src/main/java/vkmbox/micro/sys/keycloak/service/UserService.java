package vkmbox.micro.sys.keycloak.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import vkmbox.micro.sys.keycloak.dto.TokenDto;

@Service
public class UserService
{
  //@Value("${keycloak.auth-server-url}")
  private String authServerUrl = "http://localhost:8180/auth/";
  //@Value("${keycloak.realm}")
  private String realm = "micro";
  //@Value("${keycloak.resource}")
  private String user = "manager";
  private String password = "password";
  private String clientId = "micro-cli";
  private String clientSecret = "92acc719-4496-4bcf-95c4-4ac06d05490d";
  
  private RestTemplate restTemplate;
  
  @Autowired
  public UserService( RestTemplate restTemplate ) {
    this.restTemplate = restTemplate;
  }
  
  /*public UserRepresentation getUserRepresentation (UserDto userDto) {
      if ( userDto.getCredentials() == null || userDto.getCredentials().size() == 0 )
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No credentials provided");
      List<CredentialRepresentation> credentials = new ArrayList<>(userDto.getCredentials().size());
      for ( CredentialDto dto : userDto.getCredentials() ) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(dto.getType().name());
        credential.setValue(dto.getValue());
        credentials.add(credential);
      }

      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setUsername(userDto.getUsername());
      userRepresentation.setFirstName(userDto.getUsername());
      userRepresentation.setLastName(userDto.getLastName());
      userRepresentation.setEnabled(userDto.getEnabled());
      userRepresentation.setCredentials(credentials);
      return userRepresentation;
  }*/

  private String getSystemToken() {
    String uri = String.format("%srealms/%s/protocol/openid-connect/token", authServerUrl, realm);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("username", user);
    map.add("password", password);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    ResponseEntity<TokenDto> response = restTemplate.postForEntity( uri, request , TokenDto.class );
    return response.getBody() == null ? "" : response.getBody().getAccessToken();
    
  }
  
  public List<UserRepresentation> getUsers()
  {
    String uri = String.format("%sadmin/realms/%s/users", authServerUrl, realm);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", getSystemToken()));
    HttpEntity request = new HttpEntity(headers);

    ResponseEntity<List<UserRepresentation>> response = 
      restTemplate.exchange( uri, HttpMethod.GET, request
        , new ParameterizedTypeReference<List<UserRepresentation>>(){});
    return response.getBody();
    
  }
}
