package com.ib.productservice.controller;

import com.ib.productservice.entity.Batch;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import com.ib.productservice.service.batch.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private static final Logger logger = LoggerFactory.getLogger(BatchController.class);

    private final BatchService batchService;

    @Autowired
    public BatchController(BatchService batchService){
        this.batchService = batchService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllCategories(){
        try{
            return ResponseEntity.ok(batchService.obtainAll());
        }catch(Exception e){
            logger.error("Error obtaining all batches: {}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> obtainSpecificBatch(@PathVariable int id){
        try{
            Optional<Batch> batch = batchService.obtainSpecificBatchWithId(id);
            if(batch.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(batch);
        }catch(Exception e){
            logger.error("Error obtaining the batch with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }

    }

    @GetMapping("/code/{batchCode}")
    public ResponseEntity<?> obtainSpecificBatch(@PathVariable String batchCode){
        try{
            Optional<Batch> batch = batchService.obtainSpecificBatchWithCode(batchCode);
            if(batch.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(batch);
        }catch(Exception e){
            logger.error("Error obtaining the batch with code {}: {}",batchCode,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createBatch(@RequestBody Batch batch){
        try{
            Response<Statuses.CreateBatchStatus,Batch> result = batchService.createBatch(batch);
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.entity());
                case CODE_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","Batch code is in use"));
                case NEGATIVE_AMOUNT -> ResponseEntity.badRequest().body(Map.of("error","Amount must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error creating the Batch with id {}: {}", batch.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBatch(@PathVariable int id,@RequestBody Batch batch){
        try{
            Response<Statuses.UpdateBatchStatus,Batch> result = batchService.updateBatch(id,batch);
            return switch(result.status()){
                case SUCCESS -> ResponseEntity.ok(result.entity());
                case CODE_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","Batch code is in use"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case NEGATIVE_AMOUNT -> ResponseEntity.badRequest().body(Map.of("error","Amount must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error updating the Batch with id {}: {}", batch.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteBatch(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteBatchStatus,Batch> result = batchService.hardDeleteBatch(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Batch with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the Batch with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

}
