package vkmbox.micro.user.service;

import java.util.ArrayList;
import java.util.List;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vkmbox.micro.user.dto.CredentialDto;
import vkmbox.micro.user.dto.UserDto;

@Service
public class UserService
{
  public UserRepresentation getUserRepresentation (UserDto userDto) {
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
      userRepresentation.setCredentials(credentials);
      return userRepresentation;
  }
}
