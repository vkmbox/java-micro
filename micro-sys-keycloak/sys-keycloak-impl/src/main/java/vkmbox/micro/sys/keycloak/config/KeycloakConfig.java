package vkmbox.micro.sys.keycloak.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter @Setter
@ConfigurationProperties(prefix = "property.keycloak")
public class KeycloakConfig {
  private String authServerUrl;
  private String realm;
  private String user;
  private String password;
  private String clientId;
  private String clientSecret;
}
