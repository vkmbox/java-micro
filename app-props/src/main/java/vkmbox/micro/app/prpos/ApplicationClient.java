package vkmbox.micro.app.prpos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.Yaml;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationClient
{
  @Value("${spring.cloud.consul.port}")
  private Integer consulPort;
  
  @Value("${spring.cloud.consul.host}")
  private String consulHost;

  @Value("${spring.cloud.consul.config.format}")
  private String configFormat;

  @Value("${spring.cloud.consul.config.section}")
  private String configSection;
  
  public Map<String, Object> getConsulProperties() throws IOException {
    RestTemplate template = new RestTemplate();
    String uri = String.format("%s:%d/v1/kv/%s", consulHost, consulPort, configSection);
    ParameterizedTypeReference<List<ConsulResponce>> parameterizedTypeReference 
      = new ParameterizedTypeReference<List<ConsulResponce>>(){};
    ResponseEntity<List<ConsulResponce>> exchange 
      = template.exchange(uri, HttpMethod.GET, null, parameterizedTypeReference);
    List<ConsulResponce> responce = exchange.getBody();
    if ( responce == null || responce.size() != 1 || StringUtils.isEmpty(responce.get(0).value) ) {
      throw new RuntimeException( "Wrong connection to Consul node" );
    }
    
    byte[] encode = Base64Utils.decodeFromString(responce.get(0).value);
    Yaml yaml = new Yaml();  
    try( InputStream in = new ByteArrayInputStream( encode ) ) {
      Map<String, Object> tree = yaml.load( in );
      https://stackoverflow.com/questions/48744919/how-to-access-the-innernested-key-value-in-an-yaml-file-using-snakeyaml-librar
    }
  }
  
  @Getter @Setter
  static class ConsulResponce {
    @JsonProperty("Value")
    private String value;
  }
  
}
