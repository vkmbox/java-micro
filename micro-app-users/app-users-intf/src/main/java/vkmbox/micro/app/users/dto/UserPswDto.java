package vkmbox.micro.app.users.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain = true)
public class UserPswDto
{
    private String user;
    private String firstName;
    private String lastName;
    private String email;
    //Base64 array of bytes
    private byte[] password;
}
