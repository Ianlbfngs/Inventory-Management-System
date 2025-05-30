package com.ib.userservice.controller;

import com.ib.userservice.entity.Credential;
import com.ib.userservice.response.Response;
import com.ib.userservice.response.Statuses;
import com.ib.userservice.service.CredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private  final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService){
        this.credentialService = credentialService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCredential(@RequestBody Credential credential){
        try{
            Response<Statuses.CreateStatus> result = credentialService.registerCredential(credential);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok(result.credential());
                case USER_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","Username is in use"));
            };
        }catch(Exception e){
            logger.error("Error creating credential for user {}: {}", credential.getUsername(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCredential(@RequestBody Credential credential){
        try{
            Response<Statuses.LoginStatus> result = credentialService.loginCredential(credential);
            return switch (result.status()){
                case OK -> ResponseEntity.ok().body(Map.of("token",result.credential().getToken(),"username",result.credential().getUsername()));
                case SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Credential is deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case DENIED -> ResponseEntity.badRequest().body(Map.of("error","Wrong password"));
            };
        }catch(Exception e){
            logger.error("Error verifying credential with user {}: {}",credential.getUsername(),e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCredential(@RequestBody Credential credential, @PathVariable int id){
        try{
            Response<Statuses.UpdateStatus> result = credentialService.updateCredential(credential);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok(result.credential());
                case SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Credential is deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case USER_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","Username is in use"));
            };
        }catch(Exception e){
            logger.error("Error updating credential with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteCredential(@PathVariable int id){
        try{
            Response<Statuses.SoftDeleteStatus> result = credentialService.softDeleteCredential(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Credential with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case ALREADY_SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Credential is already deleted"));
            };
        }catch(Exception e){
            logger.error("Error deleting credential with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> hardDeleteCredential(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteStatus> result = credentialService.hardDeleteStatus(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Credential with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error deleting credential with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

}
