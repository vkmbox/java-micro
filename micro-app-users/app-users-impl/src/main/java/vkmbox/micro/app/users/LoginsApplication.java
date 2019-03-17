package vkmbox.micro.app.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@SpringBootApplication
public class LoginsApplication
{
    public static void main(String[] args) {
        SpringApplication.run(LoginsApplication.class, args);
    }  
}
