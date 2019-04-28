package vkmbox.micro.app.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RefreshScope
@SpringBootApplication
@EnableFeignClients(basePackages = {"vkmbox.micro.sys.keycloak.controller"})
public class AppUsersApplication
{
    public static void main(String[] args) {
        SpringApplication.run(AppUsersApplication.class, args);
    }  
}
