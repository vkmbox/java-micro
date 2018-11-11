package vkmbox.micro.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CredentialDto
{
  public enum Type { secret, password };
  
  protected Type type;
  protected String value;
}
