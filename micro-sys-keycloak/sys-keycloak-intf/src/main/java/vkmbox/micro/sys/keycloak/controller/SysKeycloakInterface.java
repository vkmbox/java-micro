package vkmbox.micro.sys.keycloak.controller;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.CredentialDto;
import org.springframework.http.HttpStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

//TODO: add validation to dto objects
@FeignClient("sys-routing/api/sys-keycloak")
@RequestMapping("v1.0.0/users")
public interface SysKeycloakInterface
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
