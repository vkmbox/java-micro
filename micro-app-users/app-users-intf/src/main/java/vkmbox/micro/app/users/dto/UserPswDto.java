package vkmbox.micro.app.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPswDto
{
    private String user;
    private String firstName;
    private String lastName;
    private String email;
    private byte[] password;
}
