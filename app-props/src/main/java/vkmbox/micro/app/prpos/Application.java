package vkmbox.micro.app.prpos;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner
{
  
    public static void main( String ... args ) {
      SpringApplication.run(Application.class, args);
      /*SpringApplication application = new SpringApplication(Application.class);
      application.setWebApplicationType(WebApplicationType.NONE); //.setWebEnvironment(false);
      application.setBannerMode(Banner.Mode.OFF);
      application.run(args);*/
    }
    
    @Autowired
    private ApplicationClient client;
    
    @Override
    public void run(String... args) throws Exception
    {
      Map<String, Object> properties = client.getConsulProperties();
      for ( Map.Entry<String, Object> entry : properties.entrySet() ) {
        if ( entry.getKey() != null && entry.getValue() != null )
          System.setProperty(entry.getKey(), entry.getValue().toString());
      }
    }
}
