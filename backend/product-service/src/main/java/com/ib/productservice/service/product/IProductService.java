package com.ib.productservice.service.product;

import com.ib.productservice.entity.Product;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> obtainAll();
    Optional<Product> obtainSpecificProduct(int id);
    Response<Statuses.CreateProductStatus,Product> createProduct(Product product);
    Response<Statuses.UpdateProductStatus,Product> updateProduct(int id,Product product);
    Response<Statuses.SoftDeleteProductStatus,Product> softDeleteProduct(int id);
    Response<Statuses.HardDeleteProductStatus,Product> hardDeleteProduct(int id);

}
