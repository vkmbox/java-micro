package vkmbox.micro.lib.ssl;

import org.apache.http.client.HttpClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.SSLContext;

public class SslUtils {
    
    public static RestTemplate sslClientRestTemplate
        (Resource trustStore, String trustStorePassword) throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
          .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
          .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClient httpClient = HttpClients.custom()
          .setSSLSocketFactory(socketFactory)
          .build();
        HttpComponentsClientHttpRequestFactory factory = 
          new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
