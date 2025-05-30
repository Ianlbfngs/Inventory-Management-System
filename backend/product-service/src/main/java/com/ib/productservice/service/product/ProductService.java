package com.ib.productservice.service.product;

import com.ib.productservice.entity.Product;
import com.ib.productservice.repository.ProductRepository;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;


    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> obtainAll() {
        return productRepository.findAllByActive(true);
    }

    @Override
    public Optional<Product> obtainSpecificProduct(int id) {
        return productRepository.findByIdAndActive(id,true);
    }

    @Override
    public Response<Statuses.CreateProductStatus, Product> createProduct(Product product) {
        if(productRepository.existsProductBySKU(product.getSKU())) return new Response<>(Statuses.CreateProductStatus.SKU_IN_USE,null);
        product.setActive(true);
        return new Response<>(Statuses.CreateProductStatus.SUCCESS,productRepository.save(product));
    }

    @Override
    public Response<Statuses.UpdateProductStatus, Product> updateProduct(int id,Product product) {
        product.setId(id);
        Optional<Product> originalProduct = productRepository.findById(product.getId());
        if(originalProduct.isEmpty()) return new Response<>(Statuses.UpdateProductStatus.NOT_FOUND,null);
        if(!originalProduct.get().isActive()) return new Response<>(Statuses.UpdateProductStatus.SOFT_DELETED,null);
        if(!originalProduct.get().getSKU().equals(product.getSKU()) && productRepository.existsProductBySKU(product.getSKU())) return new Response<>(Statuses.UpdateProductStatus.SKU_IN_USE,null);
        product.setActive(true);
        return new Response<>(Statuses.UpdateProductStatus.SUCCESS,productRepository.save(product));
    }

    @Override
    public Response<Statuses.SoftDeleteProductStatus, Product> softDeleteProduct(int id) {
        Optional<Product> toSoftDeleteProduct = productRepository.findById(id);
        if(toSoftDeleteProduct.isEmpty()) return new Response<>(Statuses.SoftDeleteProductStatus.NOT_FOUND,null);
        if(!toSoftDeleteProduct.get().isActive()) return new Response<>(Statuses.SoftDeleteProductStatus.ALREADY_SOFT_DELETED,null);
        toSoftDeleteProduct.get().setActive(false);
        return new Response<>(Statuses.SoftDeleteProductStatus.SUCCESS,productRepository.save(toSoftDeleteProduct.get()));
    }

    @Override
    public Response<Statuses.HardDeleteProductStatus, Product> hardDeleteProduct(int id) {
        if(!productRepository.existsProductById(id)) return new Response<>(Statuses.HardDeleteProductStatus.NOT_FOUND,null);
        productRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteProductStatus.SUCCESS,null);
    }
}
