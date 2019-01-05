package vkmbox.micro.sys.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenDto
{
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("expires_in")
  private Integer expiresIn;
  @JsonProperty("refresh_expires_in")
  private Integer refreshExpiresIn;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("token_type")
  private String tokenType;
}
