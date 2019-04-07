package vkmbox.micro.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter @Setter
@Accessors(chain=true)
public class UserDto
{
  private String id;
  private String username;
  private Boolean enabled;
  private String firstName;
  private String lastName;
  private String email;
  private Boolean emailVerified = true;
  
  private List<CredentialDto> credentials;
}
