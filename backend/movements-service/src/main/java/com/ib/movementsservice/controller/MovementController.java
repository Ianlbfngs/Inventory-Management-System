package com.ib.movementsservice.controller;

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
    public ResponseEntity<?> createMovement(@RequestBody Movement movement, HttpServletRequest request){
        String jwtToken = extractJwtToken(request);
        try{
            Response<Statuses.CreateMovementStatus,Movement> result = movementService.createMovement(movement,jwtToken);
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.entity());
                case BATCH_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Batch with code "+movement.getBatchCode()+" not found"));
                case SAME_STORAGE -> ResponseEntity.badRequest().body(Map.of("error","Origin and target storages cant be the same"));
                case USER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","User not found"));
                case ORIGIN_STORAGE_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Origin storage with id "+movement.getOriginStorageId()+" not found"));
                case TARGET_STORAGE_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Target storage with id "+movement.getTargetStorageId()+" not found"));
                case NEGATIVE_AMOUNT -> ResponseEntity.badRequest().body(Map.of("error","The amount of the movement must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error creating the movement: {}", e.getMessage(),e);
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
