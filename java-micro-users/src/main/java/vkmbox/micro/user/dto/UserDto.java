package vkmbox.micro.user.dto;

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
  
  private List<CredentialDto> credentials;
}
