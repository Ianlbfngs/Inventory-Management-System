package com.ib.movementsservice.controller;

import com.ib.movementsservice.dto.MovementRequest;
import com.ib.movementsservice.dto.StockDTO;
import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.service.movement.MovementService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ib.movementsservice.response.Statuses;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movements")
public class MovementController {
    private static final Logger logger = LoggerFactory.getLogger(MovementController.class);

    private final MovementService movementService;

    @Autowired
    public MovementController(MovementService movementService){
        this.movementService=movementService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllMovements(){
        try{
            return ResponseEntity.ok(movementService.obtainAll());
        }catch(Exception e){
            logger.error("Error obtaining all movements: {}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificMovement(@PathVariable int id){
        try{
            Optional<Movement> movement = movementService.obtainSpecificMovement(id);
            if(movement.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(movement);
        }catch(Exception e){
            logger.error("Error obtaining the movement with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    private String extractJwtToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createMovement(@RequestBody MovementRequest movementRequest, HttpServletRequest request){
        try{
            Movement movement = movementRequest.getMovement();
            StockDTO stockDTO = movementRequest.getStockDTO();

            String jwtToken = extractJwtToken(request);

            Response<Statuses.CreateMovementStatus,Movement> result;

            //function that selects the movement type based on the origin and target ids

            movement.getMovementType().setId(movementService.movementTypeSelector(movement.getOriginStockId(),  movement.getTargetStockId()));


            if(movement.getMovementType().getId() == 2){
                result = movementService.createMovement(movement,jwtToken);
            }else{
                result = movementService.createMovement(movement,stockDTO,jwtToken);
            }
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.entity());
                case SAME_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Origin and target stocks cant be the same"));
                case INVALID_STOCK_AMOUNT -> ResponseEntity.badRequest().body(Map.of("error","Movement stock amount must be higher than 0"));
                case NOT_ENOUGH_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Origin stock is not enough "));
                case TARGET_STOCK_NOT_FOUND -> ResponseEntity.badRequest().body(Map.of("error","Target stock not found"));
                case ORIGIN_STOCK_NOT_FOUND -> ResponseEntity.badRequest().body(Map.of("error","Origin stock not found"));
                case ERROR_UPDATING_STOCK -> ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(Map.of("error","Unable to update stock"));
                case ORIGIN_AND_TARGET_DIFFERENT_BATCH -> ResponseEntity.badRequest().body(Map.of("error","Origin and target stock must be of the same batch"));
            };
        }catch(Exception e){
            logger.error("Error creating the movement: {}", e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }


    @PutMapping("/reception/{movementId}")
    public ResponseEntity<?> updateMovementReception(@PathVariable int movementId, @RequestBody boolean receptionStatus, HttpServletRequest request){
        String jwtToken = extractJwtToken(request);
        try{
            Response<Statuses.MovementReceptionStatus,Movement> result = movementService.movementReception(movementId,receptionStatus,jwtToken);
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.entity());
                case MOVEMENT_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Movement to update reception not found"));
                case TARGET_STOCK_NOT_FOUND -> ResponseEntity.badRequest().body(Map.of("error","Target stock not found"));
                case ORIGIN_STOCK_NOT_FOUND -> ResponseEntity.badRequest().body(Map.of("error","Origin stock not found"));
                case ERROR_UPDATING_STOCK -> ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(Map.of("error","Unable to update stock"));
            };
        }catch(Exception e){
            logger.error("Error updating the movement reception status with id {}: {}",movementId, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteMovement(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteMovementStatus,Movement> result = movementService.hardDeleteProduct(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("movement with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the movement with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }
}
