package vkmbox.micro.sys.keycloak.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto
{
  private String username;
  private Boolean enabled;
  private String firstName;
  private String lastName;
  private String email;
  private Boolean emailVerified = true;
  
  private List<CredentialDto> credentials;
}
