package vkmbox.micro.sys.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class CredentialDto
{
  public enum Type { secret, password };
  
  private Type type;
  private String value;
  private Boolean temporary = false;
}
