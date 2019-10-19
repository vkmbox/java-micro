package vkmbox.micro.sys.keycloak.service;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.TokenDto;
import vkmbox.micro.lib.dto.CredentialDto;
import vkmbox.micro.lib.errors.ErrorType;
import vkmbox.micro.lib.errors.ApplicationError;
import vkmbox.micro.sys.keycloak.config.KeycloakConfig;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService
{
  private static final List<String> SYSTEM_USERS = Arrays.asList("manager");
  private static final String USER_ROLE = "user";
  
  private final RestTemplate restTemplate;
  private final KeycloakConfig params;
  
  @Autowired
  public UserService( RestTemplate restTemplate, KeycloakConfig keycloakConfig ) {
    this.restTemplate = restTemplate;
    this.params = keycloakConfig;
  }

  public List<UserRepresentation> getUsers() {
    return getUsers( null );
  }

  public String addUser(UserDto dto)
  {
    if ( getUserByName(dto.getUsername()) != null ) {
      throw new ApplicationError(ErrorType.USER_ALREADY_EXISTS, dto.getUsername());
    }
    //Fix: assogn role user in client params.getClientId()
    UserRepresentation userRepresentation = getUserRepresentationFromDto(dto);
    userRepresentation.setClientRoles(Map.of(params.getClientId(), List.of(USER_ROLE)));
    HttpEntity request = new HttpEntity( getUserRepresentationFromDto(dto), getKeycloakHeader());
    ResponseEntity<UserRepresentation> response = 
        restTemplate.exchange( getAuthPath(), HttpMethod.POST, request, UserRepresentation.class);
    List<String> location = 
        response.getHeaders() == null ? null : response.getHeaders().get(HttpHeaders.LOCATION);
    
    if ( location != null && !location.isEmpty() && !StringUtils.isEmpty(location.get(0)) ) {
        String value = location.get(0);
        return value.substring(value.lastIndexOf("/") + 1);
    } else {
        throw new ApplicationError(ErrorType.USER_NOT_CREATED);
    }
  }

  public String resetPassword(String uuid, CredentialDto dto)
  {
    if ( getUserById(uuid) == null ) {
      throw new ApplicationError(ErrorType.USER_NOT_FOUND, uuid);
    }
    
    String uri = String.format("%s/%s/reset-password", getAuthPath(), uuid);
    HttpEntity request = new HttpEntity( getCredentialRepresentationFromDto(dto), getKeycloakHeader() );
    ResponseEntity<String> response = 
      restTemplate.exchange( uri, HttpMethod.PUT, request, String.class);
    return response.getBody();
  }

  public String deleteUser(String uuid)
  {
    UserRepresentation userRepresentation = getUserById(uuid);
    if ( SYSTEM_USERS.contains(userRepresentation.getUsername()) ) {
      throw new ApplicationError(ErrorType.DELETING_USER_MANAGER_PROHIBITED);
    }

    String uri = String.format("%s/%s", getAuthPath(), uuid);
    HttpEntity request = new HttpEntity( getKeycloakHeader() );
    ResponseEntity<String> response = 
      restTemplate.exchange( uri, HttpMethod.DELETE, request, String.class);
    return response.getBody();
  }

  public TokenDto getToken(String userValue, String passwordValue) {
    String uri = String.format("%srealms/%s/protocol/openid-connect/token"
        , params.getAuthServerUrl(), params.getRealm());
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("client_id", params.getClientId());
    map.add("client_secret", params.getClientSecret());
    map.add("username", userValue);
    map.add("password", passwordValue);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    ResponseEntity<TokenDto> response = restTemplate.postForEntity( uri, request , TokenDto.class );
    return response.getBody();
  }
  
  private String getSystemToken() {
    TokenDto token = getToken(params.getUser(), params.getPassword());
    return token == null ? "" : token.getAccessToken();
  }
  
  private String getAuthPath() {
    return String.format("%sadmin/realms/%s/users", params.getAuthServerUrl(), params.getRealm());
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
  
  public UserRepresentation getUserById( String uuid ) {
    String uri = getAuthPath() + "/" + uuid;
    HttpEntity request = new HttpEntity(getKeycloakHeader());
    try {
        ResponseEntity<UserRepresentation> response = 
          restTemplate.exchange( uri, HttpMethod.GET, request
            , UserRepresentation.class);
        return response.getBody();
    } catch ( HttpClientErrorException ex ) {
        if ( ex.getStatusCode() == HttpStatus.NOT_FOUND ) {
            throw new ApplicationError(ErrorType.USER_NOT_FOUND, uuid);
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

  private UserRepresentation getUserRepresentationFromDto(UserDto userDto) {
    List<CredentialRepresentation> credentials;
    if ( userDto.getCredentials() == null || userDto.getCredentials().isEmpty() ) {
      credentials = Collections.EMPTY_LIST;
    } else {
      credentials = new ArrayList<>(userDto.getCredentials().size());
      for ( CredentialDto dto : userDto.getCredentials() ) {
        credentials.add(getCredentialRepresentationFromDto(dto));
      }
    }

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(userDto.getUsername());
    userRepresentation.setFirstName(userDto.getFirstName());
    userRepresentation.setLastName(userDto.getLastName());
    userRepresentation.setEnabled(userDto.getEnabled());
    userRepresentation.setCredentials(credentials);
    userRepresentation.setEmail(userDto.getEmail());
    userRepresentation.setEmailVerified(userDto.getEmailVerified());
    return userRepresentation;
  }

  public UserDto getUserDtoFromRepresentationNoCredentials(UserRepresentation user) {
      return new UserDto().setId(user.getId()).setUsername(user.getUsername())
        .setEnabled(user.isEnabled()).setFirstName(user.getFirstName())
        .setLastName(user.getLastName()).setEmail(user.getEmail())
        .setEmailVerified(user.isEmailVerified());
  }
  
  private CredentialRepresentation getCredentialRepresentationFromDto(CredentialDto dto) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(dto.getType().name());
    credential.setValue(dto.getValue());
    credential.setTemporary(false);
    return credential;
  }
  
}
