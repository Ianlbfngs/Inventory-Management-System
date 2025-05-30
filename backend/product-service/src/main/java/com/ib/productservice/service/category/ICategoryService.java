package com.ib.productservice.service.category;

import com.ib.productservice.entity.Category;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> obtainAll();

    Optional<Category> obtainSpecificCategory(int id);

    Category createCategory(Category category);

    Response<Statuses.UpdateCategoryStatus, Category> updateCategory(int id, Category category);

    Response<Statuses.HardDeleteCategoryStatus, Category> hardDeleteCategory(int id);

}
