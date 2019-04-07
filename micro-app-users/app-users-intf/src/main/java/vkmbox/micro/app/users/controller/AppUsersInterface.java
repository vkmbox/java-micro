package vkmbox.micro.app.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import vkmbox.micro.app.users.dto.ResetPasswordDto;
import vkmbox.micro.app.users.dto.UserPswDto;

@FeignClient("sys-routing/api/app-users")
@RequestMapping("v1.0.0/users")
public interface AppUsersInterface
{
    @PostMapping("/register-with-username")
    @ResponseStatus(HttpStatus.CREATED)  
    void registerWithUsername( @RequestBody UserPswDto registerDto );

    @PutMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    void resetPassword( @RequestBody ResetPasswordDto resetDto );
    
    /*@PutMapping("/get-token")
    @ResponseStatus(HttpStatus.OK)
    TokenDto getToken( @RequestBody UserPswDto userDto );*/
    
}
