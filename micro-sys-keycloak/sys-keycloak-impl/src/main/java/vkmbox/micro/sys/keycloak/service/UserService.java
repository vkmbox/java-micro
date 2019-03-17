package vkmbox.micro.sys.keycloak.service;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.TokenDto;
import vkmbox.micro.lib.dto.CredentialDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserService
{
  private static final String USER_ALREADY_EXISTS = "User with username=%s already exists";
  private static final String USER_NOT_CREATED = "User has not been created";
  private static final String USER_NOT_EXISTS_ID = "User with id=%s not exists";
  private static final String DELETING_MANAGER_PROHIBITED = "Deleting the user manager is prohibited";
  
  private static final List<String> SYSTEM_USERS = Arrays.asList("manager");
  
  @Value("${property.keycloak.authServerUrl}")
  private String authServerUrl;
  @Value("${property.keycloak.realm}")
  private String realm;
  @Value("${property.keycloak.user}")
  private String user;
  @Value("${property.keycloak.password}")
  private String password;
  @Value("${property.keycloak.clientId}")
  private String clientId;
  @Value("${property.keycloak.clientSecret}")
  private String clientSecret;
  
  private final RestTemplate restTemplate;
  
  @Autowired
  public UserService( RestTemplate restTemplate ) {
    this.restTemplate = restTemplate;
  }

  public List<UserRepresentation> getUsers() {
    return getUsers( null );
  }

  public String addUser(UserDto dto)
  {
    if ( getUserByName(dto.getUsername()) != null ) {
      throw new ResponseStatusException(HttpStatus.CONFLICT
        , String.format(USER_ALREADY_EXISTS, dto.getUsername()));
    }
    
    String uri = getAuthPath();
    
    HttpEntity request = new HttpEntity( getUserRepresentation(dto), getKeycloakHeader());
    ResponseEntity<String> response = restTemplate.exchange( uri, HttpMethod.POST, request, String.class);
    List<String> location = response.getHeaders() == null ? null : response.getHeaders().get("Location");
    
    if ( location != null && !location.isEmpty() && !StringUtils.isEmpty(location.get(0)) ) {
      String value = location.get(0);
      return value.substring(value.lastIndexOf("/") + 1);
    } else {
      UserRepresentation userRepresentation = getUserByName(dto.getUsername());
      if ( userRepresentation == null )
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_CREATED);
      return userRepresentation.getId();
    }
  }

  public String resetPassword(String uuid, CredentialDto dto)
  {
    if ( getUserById(uuid) == null ) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND
        , String.format(USER_NOT_EXISTS_ID, uuid));
    }
    
    String uri = String.format("%s/%s/reset-password", getAuthPath(), uuid);
    HttpEntity request = new HttpEntity( getCredentialRepresentation(dto), getKeycloakHeader() );
    ResponseEntity<String> response = 
      restTemplate.exchange( uri, HttpMethod.PUT, request, String.class);
    return response.getBody();
  }

  public String deleteUser(String uuid)
  {
    UserRepresentation userRepresentation = getUserById(uuid);
    if ( userRepresentation == null ) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND
        , String.format(USER_NOT_EXISTS_ID, uuid));
    }
    if ( SYSTEM_USERS.contains(userRepresentation.getUsername()) ) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, DELETING_MANAGER_PROHIBITED);
    }
    
    String uri = String.format("%s/%s", getAuthPath(), uuid);
    HttpEntity request = new HttpEntity( getKeycloakHeader() );
    ResponseEntity<String> response = 
      restTemplate.exchange( uri, HttpMethod.DELETE, request, String.class);
    return response.getBody();
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
  
  private String getAuthPath() {
    return String.format("%sadmin/realms/%s/users", authServerUrl, realm);
  }
  
  private HttpHeaders getKeycloakHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", getSystemToken()));
    return headers;
  }
  
  private List<UserRepresentation> getUsers( String userNameMask )
  {
    String uri = getAuthPath();
    if ( StringUtils.isEmpty(userNameMask) == false ) {
      uri += "?username=" + userNameMask;
    }
    
    HttpEntity request = new HttpEntity(getKeycloakHeader());
    ResponseEntity<List<UserRepresentation>> response = 
      restTemplate.exchange( uri, HttpMethod.GET, request
        , new ParameterizedTypeReference<List<UserRepresentation>>(){});
    List<UserRepresentation> list = response.getBody();
    return list != null ? list : Collections.EMPTY_LIST;
  }
  
  private UserRepresentation getUserById( String userId ) {
    String uri = getAuthPath() + "/" + userId;
    HttpEntity request = new HttpEntity(getKeycloakHeader());
    try {
      ResponseEntity<UserRepresentation> response = 
        restTemplate.exchange( uri, HttpMethod.GET, request
          , UserRepresentation.class);
      return response.getBody();
    } catch ( HttpClientErrorException ex ) {
      if ( ex.getStatusCode() == HttpStatus.NOT_FOUND ) {
        return null;
      } else {
        throw ex;
      }
    }
  }
  
  private UserRepresentation getUserByName( String userName ) {
    for ( UserRepresentation userRepresentation : getUsers(userName) ) {
      if ( userName.equals(userRepresentation.getUsername()) ) {
        return userRepresentation;
      }
    }
    return null;
  }

  private UserRepresentation getUserRepresentation (UserDto userDto) {
    List<CredentialRepresentation> credentials;
    if ( userDto.getCredentials() == null || userDto.getCredentials().isEmpty() ) {
      credentials = Collections.EMPTY_LIST;
    } else {
      credentials = new ArrayList<>(userDto.getCredentials().size());
      for ( CredentialDto dto : userDto.getCredentials() ) {
        credentials.add(getCredentialRepresentation(dto));
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
  
  private CredentialRepresentation getCredentialRepresentation(CredentialDto dto) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(dto.getType().name());
    credential.setValue(dto.getValue());
    credential.setTemporary(false);
    return credential;
  }
  
}

