package ru.naumen.calorietracker.service;

import ru.naumen.calorietracker.dto.ProductCreateRequest;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.dto.ProductUpdateRequest;
import ru.naumen.calorietracker.model.User;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Integer id) throws RuntimeException;
    ProductResponse createProduct(ProductCreateRequest request, User user);
    ProductResponse updateProduct(Integer id, ProductUpdateRequest request, User user) throws RuntimeException;
    void deleteProduct(Integer id) throws RuntimeException;
}
