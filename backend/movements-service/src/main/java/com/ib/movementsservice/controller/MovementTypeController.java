package com.ib.movementsservice.controller;

import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;
import com.ib.movementsservice.service.movement.MovementService;
import com.ib.movementsservice.service.movementType.MovementTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/movements/types")
public class MovementTypeController {
    private static final Logger logger = LoggerFactory.getLogger(MovementTypeController.class);

    private final MovementTypeService movementTypeService;

    @Autowired
    public MovementTypeController(MovementTypeService movementTypeService){
        this.movementTypeService=movementTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllMovementTypes(){
        try{
            return ResponseEntity.ok(movementTypeService.obtainAll());

        }catch(Exception e){
            logger.error("Error obtaining all movement types: {}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificMovementType(@PathVariable int id){
        try{
            Optional<MovementType> type = movementTypeService.obtainSpecificMovementType(id);
            if(type.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(type);
        }catch(Exception e){
            logger.error("Error obtaining the movement type with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> createMovementType(@RequestBody MovementType type){
        try{
            MovementType result = movementTypeService.createMovementType(type);
            return ResponseEntity.ok(result);

        }catch(Exception e){
            logger.error("Error creating the movement type with id {}: {}", type.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMovementType(@PathVariable int id,@RequestBody MovementType type){
        try{
            Response<Statuses.UpdateMovementTypeStatus,MovementType> result = movementTypeService.updateMovementType(id,type);
            return switch(result.status()){
                case SUCCESS -> ResponseEntity.ok(result.entity());
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error updating the movement type with id {}: {}", type.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteMovementType(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteMovementTypeStatus,MovementType> result = movementTypeService.hardDeleteMovementType(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Movement type with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the movement type with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }
}
