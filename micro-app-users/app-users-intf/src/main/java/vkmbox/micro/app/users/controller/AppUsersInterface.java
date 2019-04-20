package vkmbox.micro.app.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vkmbox.micro.app.users.dto.LinkedGroupDto;
import vkmbox.micro.app.users.dto.LinkedUserDto;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.app.users.dto.ResetPasswordDto;

@FeignClient("sys-routing/api/app-users")
@RequestMapping("v1.0.0/users")
public interface AppUsersInterface
{
    @PostMapping("/register-with-username")
    ResponseEntity<UserDto> registerWithUsername( @RequestBody UserPswDto registerDto );

    @PutMapping("/reset-password")
    ResponseEntity<UserDto> resetPassword( @RequestBody ResetPasswordDto resetDto );

    @PutMapping("/add-linked-user")
    ResponseEntity<UserDto> addLinkedUser( @RequestBody LinkedUserDto linkDto );

    @PutMapping("/remove-linked-user")
    ResponseEntity<UserDto> removeLinkedUser( @RequestBody LinkedUserDto linkDto );

    @PutMapping("/add-linked-group")
    ResponseEntity<UserDto> addLinkedGroup( @RequestBody LinkedGroupDto linkDto );

    @PutMapping("/remove-linked-group")
    ResponseEntity<UserDto> removeLinkedGroup( @RequestBody LinkedGroupDto linkDto );

    @PutMapping("/disable-user")
    ResponseEntity<UserDto> disableUser( @RequestBody String username );
    
    /*@PutMapping("/get-token")
    @ResponseStatus(HttpStatus.OK)
    TokenDto getToken( @RequestBody UserPswDto userDto );*/
    
}
