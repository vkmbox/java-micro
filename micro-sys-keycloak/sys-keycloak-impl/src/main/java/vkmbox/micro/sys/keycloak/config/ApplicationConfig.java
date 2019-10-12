package vkmbox.micro.sys.keycloak.config;

import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import vkmbox.micro.lib.ssl.SslUtils;

@Configuration
public class ApplicationConfig
{
    private final Resource trustStore;
    private final String trustStorePassword;
    
    public ApplicationConfig
        ( @Value("${keycloak.truststore}")Resource trustStore
        , @Value("${keycloak.truststore-password}")String trustStorePassword
        ) {
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
    }
    
    /*@Bean
    public RestTemplateCustomizer ribbonClientRestTemplateCustomizer
        ( RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory )
    {
        return restTemplate -> restTemplate.setRequestFactory(ribbonClientHttpRequestFactory);
    }*/
        
    @Bean 
    public RestTemplate restTemplate() throws Exception {
        return SslUtils.sslClientRestTemplate(trustStore, trustStorePassword);
    }
  
}
