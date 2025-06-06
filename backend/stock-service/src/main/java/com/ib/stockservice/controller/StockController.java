package com.ib.stockservice.controller;

import com.ib.stockservice.entity.Stock;
import com.ib.stockservice.response.Response;
import com.ib.stockservice.response.Statuses;
import com.ib.stockservice.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllStocks(){
        try{
            return ResponseEntity.ok(stockService.obtainAll());
        }catch(Exception e){
            logger.error("Error obtaining all stocks: {}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificStock(@PathVariable int id){
        try{
            Optional<Stock> product = stockService.obtainSpecificStock(id);
            if(product.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(product);
        }catch(Exception e){
            logger.error("Error obtaining the stock with id {}: {}",id,e.getMessage(),e);
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
    public ResponseEntity<?> createStock(@RequestBody Stock stock, HttpServletRequest request){
        String jwtToken = extractJwtToken(request);
        try{
            Response<Statuses.CreateStockStatus> result = stockService.createStock(stock,jwtToken);
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.stock());
                case BATCH_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Batch not found"));
                case STORAGE_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Storage not found"));
                case NEGATIVE_AVAILABLE_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Available stock must be higher than 0"));
                case NEGATIVE_PENDING_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Pending stock must be higher than 0"));
                case STOCK_ALREADY_EXISTS -> ResponseEntity.badRequest().body(Map.of("error","Stock is already created"));
            };
        }catch(Exception e){
            logger.error("Error creating the product with id {}: {}", stock.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable int id,@RequestBody Stock stock){
        try{
            Response<Statuses.UpdateStockStatus> result = stockService.updateStock(id,stock);
            return switch(result.status()){
                case SUCCESS -> ResponseEntity.ok(result.stock());
                case STOCK_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Stock not found"));
                case NEGATIVE_AVAILABLE_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Available stock must be higher than 0"));
                case NEGATIVE_PENDING_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Pending stock must be higher than 0"));
                case NOTHING_TO_UPDATE -> ResponseEntity.badRequest().body(Map.of("error","Only can update available or pending stock"));
                case NEGATIVE_STOCK -> ResponseEntity.badRequest().body(Map.of("error","Both stocks must be higher than 0"));
                case NOT_ENOUGH_STOCK -> null;
            };
        }catch(Exception e){
            logger.error("Error updating the stock with id {}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteStock(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteStockStatus> result = stockService.hardDeleteStock(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Stock with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the stock with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }


}
