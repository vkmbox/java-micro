package vkmbox.micro.app.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPswDto
{
    private String user;
    private byte[] password;
}
