package vkmbox.micro.sys.keycloak.controller;

import vkmbox.micro.lib.dto.UserDto;
import vkmbox.micro.lib.dto.CredentialDto;
import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//TODO: add validation to dto objects
@FeignClient("sys-routing/api/sys-keycloak")
@RequestMapping("/v1.0.0/keycloak-users")
public interface SysKeycloakInterface
{
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers();

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser( @PathVariable("id") String id );
    
    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser( @RequestBody UserDto userDto );
    
    @PutMapping("/{userid}/reset-password")
    public ResponseEntity<?> resetPassword
        ( @PathVariable("userid") String userid
        , @RequestBody CredentialDto credentialDto 
        );

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser( @PathVariable("id") String id );
    
}
