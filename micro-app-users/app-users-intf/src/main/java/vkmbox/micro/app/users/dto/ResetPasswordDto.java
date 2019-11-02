package vkmbox.micro.app.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordDto
{
    //Base64 array of bytes
    @JsonProperty("old")
    private byte[] oldPassword;
    //Base64 array of bytes
    @JsonProperty("new")
    private byte[] newPassword;
}
