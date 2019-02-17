package vkmbox.micro.app.prpos;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@Slf4j
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
      Map<String, String> properties = client.getConsulProperties();
      for ( Map.Entry<String, String> entry : properties.entrySet() ) {
        if ( entry.getKey() != null && entry.getValue() != null ) {
          setEnvironmentVariable(entry.getKey(), entry.getValue());
        }
      }
    }
    
    private void setEnvironmentVariable(String key, String value) 
        throws IOException, InterruptedException {
      boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
      Process process;
      if (isWindows) {
        process = Runtime.getRuntime()
          .exec(String.format("cmd.exe /c set %s=%s", key, value));
      } else {
        process = Runtime.getRuntime()
          .exec(String.format("sh -c export %s=%s", key, value));
      }
      int exitCode = process.waitFor();
      if (exitCode == 0 ) {
        log.debug("Key {} is set successfully", key);
      } else {
        log.debug("Key {} is not set", key);
      }
    }
}
