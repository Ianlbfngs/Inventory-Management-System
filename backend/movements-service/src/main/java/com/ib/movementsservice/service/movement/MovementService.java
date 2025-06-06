package com.ib.movementsservice.service.movement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.movementsservice.dto.StockDTO;
import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.entity.MovementType;
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

    private String extractUsernameFromJwtToken(String token) throws JsonProcessingException {
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
        ObjectMapper mapper = new ObjectMapper();
        Map claims = mapper.readValue(payload, Map.class);
        return claims.get("sub").toString();
    }

    private Optional<StockDTO> obtainStockById(int stockId, HttpHeaders headers){
        try{
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<StockDTO> stockGetResponse= restTemplate.exchange("http://localhost:8080/api/stock/"+stockId,HttpMethod.GET,entity, StockDTO.class);

            assert stockGetResponse.getBody() != null;

            return Optional.of(stockGetResponse.getBody());
        }catch(HttpClientErrorException.NotFound e){
            return Optional.empty();
        } catch (AssertionError e) {
            logger.error("Error stock get response body is null: {}", e.getMessage(),e);
            throw new AssertionError(e);
        }
    }

    private boolean updateStockById(StockDTO stockToUpdate,int stockId,HttpHeaders headers){
        try{
            HttpEntity<StockDTO> originalStockEntity = new HttpEntity<>(stockToUpdate,headers);
            restTemplate.exchange("http://localhost:8080/api/stock/update/"+stockId,HttpMethod.PUT,originalStockEntity,Void.class);
            return false;
        }catch(Exception e){
            logger.error("Error updating the stock with id {}: {}",stockId, e.getMessage(),e);
            return true;
        }
    }


    private int createStock(StockDTO stockDTO,int movementStockAmount, HttpHeaders headers){
        //sets of new stock pending and available values
        stockDTO.setPendingStock(movementStockAmount);
        stockDTO.setAvailableStock(0);
        //batch code and storage id are sent in the movement post request
        try{
            HttpEntity<StockDTO> newStockEntity = new HttpEntity<>(stockDTO,headers);
            ResponseEntity<StockDTO> stockCreationResponse = restTemplate.exchange("http://localhost:8080/api/stock/add",HttpMethod.POST,newStockEntity,StockDTO.class);
            assert stockCreationResponse.getBody() != null;
            return stockCreationResponse.getBody().getId();
        }catch (AssertionError e) {
            logger.error("Error creating stock, response body null: {}", e.getMessage(),e);
            throw new AssertionError(e);
        }

    }

    @Override
    public Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement, String jwtToken) throws JsonProcessingException {
        //overcharge used only when the movement is from an storage to an external entity (like a supermarket)
        //stock -> external entity
        //only needs to update stock

        if(movement.getStockAmount() <=0) return new Response<>(Statuses.CreateMovementStatus.INVALID_STOCK_AMOUNT,null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        Optional<StockDTO> originStock = obtainStockById(movement.getOriginStockId(), headers);

        if(originStock.isEmpty()) return new Response<>(Statuses.CreateMovementStatus.ORIGIN_STOCK_NOT_FOUND,null);
        if((originStock.get().getAvailableStock() - movement.getStockAmount()) < 0)return new Response<>(Statuses.CreateMovementStatus.NOT_ENOUGH_STOCK,null);

        originStock.get().setAvailableStock(originStock.get().getAvailableStock() - movement.getStockAmount());

        if(updateStockById(originStock.get(), movement.getOriginStockId(),headers)) return new Response<>(Statuses.CreateMovementStatus.ERROR_UPDATING_STOCK,null);

        movement.setCreatedByUser(extractUsernameFromJwtToken(jwtToken));

        return new Response<>(Statuses.CreateMovementStatus.SUCCESS,movementRepository.save(movement));
    }

    @Override
    public Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement,StockDTO stockDTO, String jwtToken) throws JsonProcessingException {
        //overcharge used when the movement is supplier -> stock or stock -> stock
        //first case always needs to create a new stock
        //second case may need creating a new stock, which is determined by the stockDto parameter

        if(movement.getStockAmount() <=0) return new Response<>(Statuses.CreateMovementStatus.INVALID_STOCK_AMOUNT,null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);


        movement.setCreatedByUser(extractUsernameFromJwtToken(jwtToken));


        if(movement.getMovementType().getId()==1){ //first case (supplier -> stock)
            movement.setTargetStockId(createStock(stockDTO,movement.getStockAmount(),headers)); // creates stock for the new batch to be received
        }else{ //second case (stock -> stock)
            if(movement.getOriginStockId() ==  movement.getTargetStockId()) return new Response<>(Statuses.CreateMovementStatus.SAME_STOCK,null);

            Optional<StockDTO> originStock = obtainStockById(movement.getOriginStockId(),headers);

            if(originStock.isEmpty()) return new Response<>(Statuses.CreateMovementStatus.ORIGIN_STOCK_NOT_FOUND,null);
            if(originStock.get().getAvailableStock() - movement.getStockAmount() < 0)return new Response<>(Statuses.CreateMovementStatus.NOT_ENOUGH_STOCK,null);

            if(stockDTO == null){ //subcase where stockDTO parameter is null, so no new stock is needed
                Optional<StockDTO> targetStock = obtainStockById(movement.getTargetStockId(),headers);

                if(targetStock.isEmpty()) return new Response<>(Statuses.CreateMovementStatus.TARGET_STOCK_NOT_FOUND,null);

                targetStock.get().setPendingStock(targetStock.get().getPendingStock() + movement.getStockAmount());

                if(updateStockById(targetStock.get(), movement.getTargetStockId(),headers)) return new Response<>(Statuses.CreateMovementStatus.ERROR_UPDATING_STOCK,null);
            }else{ //subcase where target stock must be created
                stockDTO.setBatchCode(originStock.get().getBatchCode());
                movement.setTargetStockId(createStock(stockDTO,movement.getStockAmount(),headers));
            }


            originStock.get().setAvailableStock(originStock.get().getAvailableStock() - movement.getStockAmount());

            if(updateStockById(originStock.get(), movement.getOriginStockId(),headers)) return new Response<>(Statuses.CreateMovementStatus.ERROR_UPDATING_STOCK,null);
        }

        return new Response<>(Statuses.CreateMovementStatus.SUCCESS,movementRepository.save(movement));
    }

    @Override
    public Response<Statuses.MovementReceptionStatus, Movement> movementReception(int movementId, boolean receptionStatus, String jwtToken) {
        Optional<Movement> movement = movementRepository.findById(movementId);
        if(movement.isEmpty()) return new Response<>(Statuses.MovementReceptionStatus.MOVEMENT_NOT_FOUND,null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        Optional<StockDTO> targetStock;

        if(receptionStatus){ //received
             targetStock = obtainStockById(movement.get().getTargetStockId(),headers);

            if(targetStock.isEmpty()) return new Response<>(Statuses.MovementReceptionStatus.TARGET_STOCK_NOT_FOUND,null);

            targetStock.get().setAvailableStock(targetStock.get().getAvailableStock() + movement.get().getStockAmount());
            targetStock.get().setPendingStock(targetStock.get().getPendingStock() - movement.get().getStockAmount());

            if(updateStockById(targetStock.get(),movement.get().getTargetStockId(),headers)) return new Response<>(Statuses.MovementReceptionStatus.ERROR_UPDATING_STOCK,null);
            movement.get().setStatus(1);
        }else{ //cancelled = reverts the movement
            targetStock = obtainStockById(movement.get().getTargetStockId(),headers);
            if(targetStock.isEmpty()) return new Response<>(Statuses.MovementReceptionStatus.TARGET_STOCK_NOT_FOUND,null);

            Optional<StockDTO> originStock = obtainStockById(movement.get().getOriginStockId(),headers);
            if(originStock.isEmpty()) return new Response<>(Statuses.MovementReceptionStatus.ORIGIN_STOCK_NOT_FOUND,null);

            targetStock.get().setPendingStock(targetStock.get().getPendingStock() - movement.get().getStockAmount());
            originStock.get().setAvailableStock(originStock.get().getAvailableStock() + movement.get().getStockAmount());


            if(updateStockById(targetStock.get(),movement.get().getTargetStockId(),headers) || updateStockById(originStock.get(),movement.get().getOriginStockId(),headers)) return new Response<>(Statuses.MovementReceptionStatus.ERROR_UPDATING_STOCK,null);
            movement.get().setStatus(-1);
        }


        return new Response<>(Statuses.MovementReceptionStatus.SUCCESS,movementRepository.save(movement.get()));
    }

    @Override
    public Response<Statuses.HardDeleteMovementStatus, Movement> hardDeleteProduct(int id) {
        if(!movementRepository.existsById(id))return new Response<>(Statuses.HardDeleteMovementStatus.NOT_FOUND,null);
        movementRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteMovementStatus.SUCCESS,null);
    }




}
