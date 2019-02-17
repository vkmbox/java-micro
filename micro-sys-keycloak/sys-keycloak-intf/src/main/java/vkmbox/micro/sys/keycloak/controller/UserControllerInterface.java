package vkmbox.micro.sys.keycloak.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import vkmbox.micro.sys.keycloak.dto.UserDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vkmbox.micro.sys.keycloak.dto.CredentialDto;

import java.util.List;

//TODO: add validation to dto objects
@FeignClient("sys-routing/api/sys-keycloak")
@RequestMapping("v1.0.0/users")
public interface UserControllerInterface
{
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRepresentation> getUsers();
    
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser( @RequestBody UserDto userDto );
    
    @PutMapping("/{uuid}/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public String resetPassword
        ( @PathVariable("uuid") String uuid
        , @RequestBody CredentialDto credentialDto 
        );

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser( @PathVariable("uuid") String uuid );
    
}
