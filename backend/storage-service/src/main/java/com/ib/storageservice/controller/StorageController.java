package com.ib.storageservice.controller;

import com.ib.storageservice.entity.Storage;
import com.ib.storageservice.response.Response;
import com.ib.storageservice.response.Statuses;
import com.ib.storageservice.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService){
        this.storageService = storageService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllStorages(){
        try{
            return ResponseEntity.ok(storageService.obtainAll());
        }catch(Exception e){
            logger.error("Error obtaining all storages:{}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificStorage(@PathVariable int id){
        try{
            Optional<Storage> storage = storageService.obtainSpecificStorage(id);
            if(storage.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(storage);
        }catch(Exception e){
            logger.error("Error obtaining the storage with id {}:{}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> createStorage(@RequestBody Storage storage){
        try{
            Response<Statuses.CreateStorageStatus> result = storageService.createStorage(storage);
            return switch (result.status()){
                case SUCCESS ->   ResponseEntity.ok(result.storage());
                case NEGATIVE_CAPACITY -> ResponseEntity.badRequest().body(Map.of("error","Capacity must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error creating the storage with id {}:{}", storage.getId(),e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStorage(@PathVariable int id, @RequestBody Storage storage){
        try{
            Response<Statuses.UpdateStorageStatus> result = storageService.updateStatus(id,storage);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok(result.storage());
                case SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Storage is soft deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case NEGATIVE_CAPACITY -> ResponseEntity.badRequest().body(Map.of("error","Capacity must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error updating the storage with id {}: {}", storage.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteStorage(@PathVariable int id){
        try{
            Response<Statuses.SoftDeleteStorageStatus> result = storageService.softDeleteStorage(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok(result.storage());
                case ALREADY_SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Storage is already soft deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error soft deleting the storage with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteStorage(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteStorageStatus> result = storageService.hardDeleteStorage(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Storage with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the storage with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }



}
