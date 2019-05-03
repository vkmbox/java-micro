package vkmbox.micro.app.users.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter 
@Setter
@Component
public class UserContext {
    //public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTHORIZATION_HEADER     = "Authorization";
    //public static final String USER_ID        = "tmx-user-id";
    //public static final String ORG_ID         = "tmx-org-id";

    //private String correlationId= new String();
    private String authToken; //= new String();
    //private String userId = new String();
    //private String orgId = new String();

}