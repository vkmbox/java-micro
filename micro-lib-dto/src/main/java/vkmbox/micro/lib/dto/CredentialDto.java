package vkmbox.micro.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain=true)
public class CredentialDto
{
  public enum Type { secret, password };
  
  private Type type;
  private String value;
  private Boolean temporary = false;
}
