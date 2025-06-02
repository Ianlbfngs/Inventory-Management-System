package com.ib.movementsservice.service.movement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.movementsservice.controller.MovementController;
import com.ib.movementsservice.dto.CredentialDTO;
import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.repository.MovementRespository;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class MovementService implements IMovementService{
    private static final Logger logger = LoggerFactory.getLogger(MovementService.class);

    private final RestTemplate restTemplate;

    private final MovementRespository movementRepository;

    @Autowired
    public MovementService(RestTemplate restTemplate, MovementRespository movementRepository){
        this.restTemplate = restTemplate;
        this.movementRepository = movementRepository;
    }

    @Override
    public Object obtainAll() {
        return movementRepository.findAll();
    }

    @Override
    public Optional<Movement> obtainSpecificMovement(int id) {
        return movementRepository.findById(id);
    }

    public String extractUsernameFromJwtToken(String token) throws JsonProcessingException {
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
        ObjectMapper mapper = new ObjectMapper();
        Map claims = mapper.readValue(payload, Map.class);
        return claims.get("sub").toString();
    }

    @Override
    public Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement, String jwtToken) {
        if(movement.getOriginStorageId() ==  movement.getTargetStorageId()) return new Response<>(Statuses.CreateMovementStatus.SAME_STORAGE,null);
        if(movement.getAmount() <=0) return new Response<>(Statuses.CreateMovementStatus.NEGATIVE_AMOUNT,null);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<Void> responseBatch = restTemplate.exchange("http://localhost:8080/api/batches/code/" + movement.getBatchCode(), HttpMethod.GET,entity, Void.class);

            String username = extractUsernameFromJwtToken(jwtToken);

            try{
                ResponseEntity<CredentialDTO> responseUser = restTemplate.exchange("http://localhost:8080/api/credentials/username/" + username, HttpMethod.GET,entity, CredentialDTO.class);
                assert responseUser.getBody() != null;
                movement.setUserId(responseUser.getBody().getId());

                if(movement.getOriginStorageId()==0){ // id 0 means that the movement comes from an outer source (like a supplier)
                    try{
                        ResponseEntity<Void> responseTargetStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + movement.getTargetStorageId(), HttpMethod.GET,entity, Void.class);
                        return new Response<>(Statuses.CreateMovementStatus.SUCCESS,movementRepository.save(movement));

                    }catch(HttpClientErrorException.NotFound e){
                        logger.error("Error requesting the target storage with id {}: {}",movement.getTargetStorageId(),e.getMessage(),e);
                        return new Response<>(Statuses.CreateMovementStatus.TARGET_STORAGE_NOT_FOUND,null);
                    }
                }else if(movement.getTargetStorageId() ==0){ // id 0 means that the movement goes to an outer source/not to a storage (like a supermarket)
                    try{
                        ResponseEntity<Void> responseOriginStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + movement.getOriginStorageId(), HttpMethod.GET,entity, Void.class);
                        return new Response<>(Statuses.CreateMovementStatus.SUCCESS,movementRepository.save(movement));

                    }catch(HttpClientErrorException.NotFound e){
                        logger.error("Error requesting the origin storage with id {}: {}",movement.getOriginStorageId(),e.getMessage(),e);
                        return new Response<>(Statuses.CreateMovementStatus.ORIGIN_STORAGE_NOT_FOUND,null);
                    }
                }else{ //case where both the origin and the target are != 0, so its a movement between two storages
                    try{
                        ResponseEntity<Void> responseOriginStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + movement.getOriginStorageId(), HttpMethod.GET,entity, Void.class);
                        try{
                            ResponseEntity<Void> responseTargetStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + movement.getTargetStorageId(), HttpMethod.GET,entity, Void.class);
                            return new Response<>(Statuses.CreateMovementStatus.SUCCESS,movementRepository.save(movement));

                        }catch(HttpClientErrorException.NotFound e){
                            logger.error("Error requesting the origin storage with id {}: {}",movement.getOriginStorageId(),e.getMessage(),e);
                            return new Response<>(Statuses.CreateMovementStatus.TARGET_STORAGE_NOT_FOUND,null);
                        }
                    }catch(HttpClientErrorException.NotFound e){
                        logger.error("Error requesting the target storage with id {}: {}",movement.getTargetStorageId(),e.getMessage(),e);
                        return new Response<>(Statuses.CreateMovementStatus.ORIGIN_STORAGE_NOT_FOUND,null);
                    }
                }

            }catch(HttpClientErrorException.NotFound e){
                logger.error("Error requesting the user {}: {}",username,e.getMessage(),e);
                return new Response<>(Statuses.CreateMovementStatus.USER_NOT_FOUND,null);
            }

        }catch(HttpClientErrorException.NotFound e){
            logger.error("Error requesting the batch with code {}: {}",movement.getBatchCode(),e.getMessage(),e);
            return new Response<>(Statuses.CreateMovementStatus.BATCH_NOT_FOUND,null);
        } catch (JsonProcessingException e) {
            logger.error("Error requesting the batch with code {}: {}",movement.getBatchCode(),e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response<Statuses.HardDeleteMovementStatus, Movement> hardDeleteProduct(int id) {
        if(!movementRepository.existsById(id))return new Response<>(Statuses.HardDeleteMovementStatus.NOT_FOUND,null);
        movementRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteMovementStatus.SUCCESS,null);
    }
}
