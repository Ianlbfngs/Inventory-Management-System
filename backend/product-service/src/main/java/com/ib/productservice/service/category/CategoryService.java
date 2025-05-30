package com.ib.productservice.service.category;

import com.ib.productservice.entity.Category;
import com.ib.productservice.repository.CategoryRepository;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> obtainAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> obtainSpecificCategory(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return  categoryRepository.save(category);
    }

    @Override
    public Response<Statuses.UpdateCategoryStatus, Category> updateCategory(int id, Category category) {
        category.setId(id);
        Optional<Category> originalCategory = categoryRepository.findById(id);
        if(originalCategory.isEmpty()) return new Response<>(Statuses.UpdateCategoryStatus.NOT_FOUND,null);
        return new Response<>(Statuses.UpdateCategoryStatus.SUCCESS,categoryRepository.save(category));
    }

    @Override
    public Response<Statuses.HardDeleteCategoryStatus, Category> hardDeleteCategory(int id) {
        if(!categoryRepository.existsById(id))return new Response<>(Statuses.HardDeleteCategoryStatus.NOT_FOUND,null);
        categoryRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteCategoryStatus.SUCCESS,null);

    }
}
