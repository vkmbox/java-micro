package vkmbox.micro.sys.keycloak.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig
{
    @Bean
    public RestTemplateCustomizer ribbonClientRestTemplateCustomizer
        ( RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory )
    {
        return restTemplate -> restTemplate.setRequestFactory(ribbonClientHttpRequestFactory);
    }
        
    @Bean 
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }
  
}
