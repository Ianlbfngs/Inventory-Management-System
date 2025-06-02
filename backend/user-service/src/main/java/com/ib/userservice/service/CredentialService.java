package com.ib.userservice.service;

import com.ib.userservice.entity.Credential;
import com.ib.userservice.repository.CredentialRepository;
import com.ib.userservice.response.Response;
import com.ib.userservice.response.Statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class CredentialService implements ICredentialService{

    private final CredentialRepository credentialRepository;
    private final JwtService jwtService;


    @Autowired
    public CredentialService(CredentialRepository credentialRepository, JwtService jwtService){
        this.credentialRepository = credentialRepository;
        this.jwtService = jwtService;
    }



    @Override
    public Response<Statuses.CreateStatus> registerCredential(Credential credential) {
        if(credentialRepository.existsByUsername(credential.getUsername())) return new Response<>(Statuses.CreateStatus.USER_IN_USE,credential);
        credential.setActive(true);
        Credential result = credentialRepository.save(credential);
        return new Response<>(Statuses.CreateStatus.SUCCESS,credential);
    }

    @Override
    public Response<Statuses.LoginStatus> loginCredential(Credential credential) {
        Optional<Credential> completeCredential = credentialRepository.findCredentialByUsername(credential.getUsername());
        if(completeCredential.isEmpty()) return new Response<>(Statuses.LoginStatus.NOT_FOUND,null);
        if(!completeCredential.get().isActive()) return new Response<>(Statuses.LoginStatus.SOFT_DELETED,null);
        if(!credential.getPassword().equals(completeCredential.get().getPassword())) return new Response<>(Statuses.LoginStatus.DENIED,null);
        Credential loggedCredential = new Credential();
        loggedCredential.setToken(jwtService.generateToken(credential.getUsername()));
        loggedCredential.setUsername(credential.getUsername());
        loggedCredential.setActive(true);
        return new Response<>(Statuses.LoginStatus.OK,loggedCredential);

    }

    @Override
    public Response<Statuses.UpdateStatus> updateCredential(Credential credential) {
        Optional<Credential> originalCredential = credentialRepository.findCredentialById(credential.getId());
        if(originalCredential.isEmpty()) return new Response<>(Statuses.UpdateStatus.NOT_FOUND,null);
        if(!originalCredential.get().isActive()) return new Response<>(Statuses.UpdateStatus.SOFT_DELETED,null);
        if(!originalCredential.get().getUsername().equals(credential.getUsername()) && credentialRepository.existsByUsername(credential.getUsername())) return new Response<>(Statuses.UpdateStatus.USER_IN_USE,null);
        credential.setActive(true);
        return new Response<>(Statuses.UpdateStatus.SUCCESS,credential);
    }

    @Override
    public Response<Statuses.SoftDeleteStatus> softDeleteCredential(int id) {
        Optional<Credential> credentialToDelete = credentialRepository.findCredentialById(id);
        if(credentialToDelete.isEmpty()) return new Response<>(Statuses.SoftDeleteStatus.NOT_FOUND,null);
        if(!credentialToDelete.get().isActive()) return new Response<>(Statuses.SoftDeleteStatus.ALREADY_SOFT_DELETED,null);
        credentialToDelete.get().setActive(false);
        credentialRepository.save(credentialToDelete.get());
        return new Response<>(Statuses.SoftDeleteStatus.SUCCESS,null);
    }

    @Override
    public Response<Statuses.HardDeleteStatus> hardDeleteStatus(int id) {
        if(!credentialRepository.existsById(id)) return new Response<>(Statuses.HardDeleteStatus.NOT_FOUND,null);
        credentialRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteStatus.SUCCESS,null);
    }

    @Override
    public int findCredentialIdByUsername(String username) throws HttpClientErrorException {
        Optional<Credential> credential = credentialRepository.findCredentialByUsername(username);
        if(credential.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return credential.get().getId();

    }
}
