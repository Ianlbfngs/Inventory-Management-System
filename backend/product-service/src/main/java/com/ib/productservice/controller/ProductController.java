package com.ib.productservice.controller;

import com.ib.productservice.entity.Product;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import com.ib.productservice.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> obtainAllProducts(){
        try{
            return ResponseEntity.ok(productService.obtainAll());
        }catch(Exception e){
            logger.error("Error obtaining all products: {}",e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificProduct(@PathVariable int id){
        try{
            Optional<Product> product = productService.obtainSpecificProduct(id);
            if(product.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(product);
        }catch(Exception e){
            logger.error("Error obtaining the product with id {}: {}",id,e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        try{
            Response<Statuses.CreateProductStatus,Product> result = productService.createProduct(product);
            return switch (result.status()){
                case SUCCESS ->  ResponseEntity.ok(result.entity());
                case SKU_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","SKU is in use"));
                case NEGATIVE_WEIGHT -> ResponseEntity.badRequest().body(Map.of("error","Weight must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error creating the product with id {}: {}", product.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id,@RequestBody Product product){
        try{
            Response<Statuses.UpdateProductStatus,Product> result = productService.updateProduct(id,product);
            return switch(result.status()){
                case SUCCESS -> ResponseEntity.ok(result.entity());
                case SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Product is soft deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
                case SKU_IN_USE -> ResponseEntity.badRequest().body(Map.of("error","SKU is in use"));
                case NEGATIVE_WEIGHT -> ResponseEntity.badRequest().body(Map.of("error","Weight must be higher than 0"));
            };
        }catch(Exception e){
            logger.error("Error updating the product with id {}: {}", product.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteProduct(@PathVariable int id){
        try{
            Response<Statuses.SoftDeleteProductStatus,Product> result = productService.softDeleteProduct(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok(result.entity());
                case ALREADY_SOFT_DELETED -> ResponseEntity.badRequest().body(Map.of("error","Product is already soft deleted"));
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error soft deleting the product with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteProduct(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteProductStatus,Product> result = productService.hardDeleteProduct(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("Product with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the product with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }
}
