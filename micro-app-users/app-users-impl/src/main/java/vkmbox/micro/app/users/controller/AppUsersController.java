package vkmbox.micro.app.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.app.users.dto.UserPswDto;
import vkmbox.micro.app.users.dto.ResetPasswordDto;
import vkmbox.micro.app.users.service.AppUsersService;

import java.net.URI;

@Slf4j
@RestController
public class AppUsersController implements AppUsersInterface
{
    
    private final AppUsersService service;
    
    @Autowired
    public AppUsersController(AppUsersService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<UserDto> registerWithUsername(@RequestBody UserPswDto registerDto)
    {
        UserDto dto = service.registerWithUsername(registerDto);
        URI uri = MvcUriComponentsBuilder.fromController(getClass())
          .path("/{id}").buildAndExpand(dto.getId()).toUri();        
        return ResponseEntity.created(uri).body(dto);
    }

    @Override
    public ResponseEntity<UserDto> disableUser(@PathVariable String id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> getUser(@PathVariable String id)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> resetPassword(@PathVariable String id, @RequestBody ResetPasswordDto resetDto)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> addLinkedUser(@PathVariable String id, @RequestBody String linked)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> removeLinkedUser(@PathVariable String id, @RequestBody String linked)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> addLinkedGroup(@PathVariable String id, @RequestBody String linked)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<UserDto> removeLinkedGroup(@PathVariable String id, @RequestBody String linked)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
