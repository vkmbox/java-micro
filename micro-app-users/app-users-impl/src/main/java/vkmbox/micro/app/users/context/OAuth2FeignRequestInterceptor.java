package vkmbox.micro.app.users.context;

import feign.RequestTemplate;
import feign.RequestInterceptor;
import org.springframework.stereotype.Component;

@Component
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (!template.headers().containsKey(UserContext.AUTHORIZATION_HEADER)) {
            template.header(UserContext.AUTHORIZATION_HEADER, UserContextHolder.getContext().getAuthToken());
        }
    }
}