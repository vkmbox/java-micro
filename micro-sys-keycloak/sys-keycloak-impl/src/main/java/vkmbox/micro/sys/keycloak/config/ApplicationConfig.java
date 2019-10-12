package vkmbox.micro.sys.keycloak.config;

//import org.springframework.boot.web.client.RestTemplateCustomizer;
//import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;

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
        SSLContext sslContext = new SSLContextBuilder()
          .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
          .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClient httpClient = HttpClients.custom()
          .setSSLSocketFactory(socketFactory)
          .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
  
}
