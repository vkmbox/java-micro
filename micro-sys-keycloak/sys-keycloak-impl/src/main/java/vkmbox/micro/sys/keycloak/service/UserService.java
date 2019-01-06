package vkmbox.micro.sys.keycloak.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.util.StringUtils;
import vkmbox.micro.sys.keycloak.dto.CredentialDto;
import vkmbox.micro.sys.keycloak.dto.TokenDto;
import vkmbox.micro.sys.keycloak.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserService
{
  @Value("${property.keycloak.authServerUrl}")
  private String authServerUrl; //= "http://192.168.56.101:8180/auth/";
  @Value("${property.keycloak.realm}")
  private String realm; //= "micro";
  @Value("${property.keycloak.user}")
  private String user; //= "manager";
  @Value("${property.keycloak.password}")
  private String password; //= "password";
  @Value("${property.keycloak.clientId}")
  private String clientId; //= "micro-cli";
  @Value("${property.keycloak.clientSecret}")
  private String clientSecret; //= "92acc719-4496-4bcf-95c4-4ac06d05490d";
  
  private RestTemplate restTemplate;
  
  @Autowired
  public UserService( RestTemplate restTemplate ) {
    this.restTemplate = restTemplate;
  }

  public UserRepresentation getUserRepresentation (UserDto userDto) {
      List<CredentialRepresentation> credentials;
      if ( userDto.getCredentials() == null || userDto.getCredentials().isEmpty() ) {
        credentials = Collections.EMPTY_LIST;
      } else {
        credentials = new ArrayList<>(userDto.getCredentials().size());
        for ( CredentialDto dto : userDto.getCredentials() ) {
          CredentialRepresentation credential = new CredentialRepresentation();
          credential.setType(dto.getType().name());
          credential.setValue(dto.getValue());
          credential.setTemporary(false);
          credentials.add(credential);
        }
      }

      UserRepresentation userRepresentation = new UserRepresentation();
      userRepresentation.setUsername(userDto.getUsername());
      userRepresentation.setFirstName(userDto.getFirstName());
      userRepresentation.setLastName(userDto.getLastName());
      userRepresentation.setEnabled(userDto.getEnabled());
      userRepresentation.setCredentials(credentials);
      return userRepresentation;
  }

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

  public List<UserRepresentation> getUsers( ) {
    return getUsers( null );
  }
  
  public List<UserRepresentation> getUsers( String userName )
  {
    String uri = String.format("%sadmin/realms/%s/users", authServerUrl, realm);
    if ( StringUtils.isEmpty(userName) == false ) {
      uri += "?username=test" + userName;
    }
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", getSystemToken()));
    HttpEntity request = new HttpEntity(headers);

    ResponseEntity<List<UserRepresentation>> response = 
      restTemplate.exchange( uri, HttpMethod.GET, request
        , new ParameterizedTypeReference<List<UserRepresentation>>(){});
    return response.getBody();
    
  }

  public String addUser(UserDto dto)
  {
    String uri = String.format("%sadmin/realms/%s/users", authServerUrl, realm);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", getSystemToken()));
    HttpEntity request = new HttpEntity( getUserRepresentation(dto), headers);

    ResponseEntity<String> response = 
      restTemplate.exchange( uri, HttpMethod.POST, request, String.class);
    List<String> location = response.getHeaders() == null ? null : response.getHeaders().get("Location");
    
    if ( location != null && !location.isEmpty() && !StringUtils.isEmpty(location.get(0)) ) {
      String value = location.get(0);
      return value.substring(value.lastIndexOf("/") + 1);
    } else {
      List<UserRepresentation> users = getUsers(dto.getUsername());
      if ( users == null || users.isEmpty() )
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User has not been created");
      return users.get(0).getId();
    }
  }
}
//
