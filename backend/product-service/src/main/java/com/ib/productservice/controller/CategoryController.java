package com.ib.productservice.controller;

import com.ib.productservice.entity.Category;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import com.ib.productservice.service.category.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public List<Category> obtainAllCategories(){
        return categoryService.obtainAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtainSpecificCategory(@PathVariable int id){
        Optional<Category> category = categoryService.obtainSpecificCategory(id);
        if(category.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        try{
            Category result = categoryService.createCategory(category);
            return ResponseEntity.ok(result);

        }catch(Exception e){
            logger.error("Error creating the category with id {}: {}", category.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id,@RequestBody Category category){
        try{
            Response<Statuses.UpdateCategoryStatus,Category> result = categoryService.updateCategory(id,category);
            return switch(result.status()){
                case SUCCESS -> ResponseEntity.ok(result.entity());
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error updating the category with id {}: {}", category.getId(), e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteCategory(@PathVariable int id){
        try{
            Response<Statuses.HardDeleteCategoryStatus,Category> result = categoryService.hardDeleteCategory(id);
            return switch (result.status()){
                case SUCCESS -> ResponseEntity.ok("category with id "+id+" successfully deleted");
                case NOT_FOUND -> ResponseEntity.notFound().build();
            };
        }catch(Exception e){
            logger.error("Error hard deleting the category with id{}: {}", id, e.getMessage(),e);
            return ResponseEntity.status(500).body("Something went wrong");
        }
    }

}
