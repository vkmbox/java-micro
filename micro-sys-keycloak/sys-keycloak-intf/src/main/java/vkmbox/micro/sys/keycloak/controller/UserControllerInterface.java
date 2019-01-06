package vkmbox.micro.sys.keycloak.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import vkmbox.micro.sys.keycloak.dto.UserDto;

import java.util.List;

@FeignClient("sys-routing/api/sys-keycloak")
public interface UserControllerInterface
{
    @GetMapping("/all")
    public List<UserRepresentation> getUsers();
    
    @PostMapping("/add")
    public String addUser( @RequestBody UserDto userDto );
  
}
