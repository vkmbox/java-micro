package vkmbox.micro.app.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordDto
{
    @JsonProperty("user")
    private String user;
    @JsonProperty("old")
    private byte[] oldPassword;
    @JsonProperty("new")
    private byte[] newPassword;
}
