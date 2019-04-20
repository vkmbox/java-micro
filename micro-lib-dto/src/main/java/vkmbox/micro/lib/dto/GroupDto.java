package vkmbox.micro.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain=true)
public class GroupDto
{
  private String id;
  private String groupname;
  private Boolean enabled;
    
}
