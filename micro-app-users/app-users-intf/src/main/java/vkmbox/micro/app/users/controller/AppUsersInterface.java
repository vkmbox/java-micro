package vkmbox.micro.app.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.app.users.dto.ResetPasswordDto;
import vkmbox.micro.lib.dto.TokenDto;

@FeignClient("sys-routing/api/app-users")
@RequestMapping("/v1.0.0/users")
public interface AppUsersInterface
{
    @PostMapping("/register-with-username")
    ResponseEntity<UserDto> registerWithUsername( @RequestBody UserPswDto registerDto );

    @PutMapping("/reset-password/{id}")
    ResponseEntity<UserDto> resetPassword( @PathVariable String id, @RequestBody ResetPasswordDto resetDto );

    @PutMapping("/add-linked-user/{id}")
    ResponseEntity<UserDto> addLinkedUser( @PathVariable String id, @RequestBody String linked );

    @PutMapping("/remove-linked-user/{id}")
    ResponseEntity<UserDto> removeLinkedUser( @PathVariable String id, @RequestBody String linked );

    @PutMapping("/add-linked-group/{id}")
    ResponseEntity<UserDto> addLinkedGroup( @PathVariable String id, @RequestBody String linked );

    @PutMapping("/remove-linked-group/{id}")
    ResponseEntity<UserDto> removeLinkedGroup( @PathVariable String id, @RequestBody String linked );

    @PutMapping("/disable-user/{id}")
    ResponseEntity<UserDto> disableUser( @PathVariable String id );
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser( @PathVariable String id );
    
    @PutMapping("/get-token")
    ResponseEntity<TokenDto> getToken( @RequestBody UserPswDto userDto );
    
}
