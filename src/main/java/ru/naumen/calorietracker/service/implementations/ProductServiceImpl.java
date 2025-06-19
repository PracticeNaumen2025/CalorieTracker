package ru.naumen.calorietracker.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.calorietracker.dto.ProductCreateRequest;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.dto.ProductUpdateRequest;
import ru.naumen.calorietracker.mapper.ProductMapper;
import ru.naumen.calorietracker.model.Category;
import ru.naumen.calorietracker.model.Product;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.model.elastic.ProductSearchDocument;
import ru.naumen.calorietracker.repository.ProductRepository;
import ru.naumen.calorietracker.repository.search.ProductSearchRepository;
import ru.naumen.calorietracker.service.CategoryService;
import ru.naumen.calorietracker.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductSearchRepository productSearchRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllByIsDeletedFalse()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Integer id) throws RuntimeException {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден или удален"));
        return productMapper.toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, User user) throws RuntimeException {
        Category category = categoryService.getCategoryById(request.getCategoryId());
        if (category == null) {
            throw new RuntimeException("Категория не найдена");
        }

        Product product = productMapper.toProduct(request);
        product.setCategory(category);
        product.setUser(user);

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductUpdateRequest request, User user) throws RuntimeException {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден или удален"));

        if (!user.getUserId().equals(product.getUser().getUserId()) && user.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Нет прав для редактирования продукта");
        }

        Category category = categoryService.getCategoryById(request.getCategoryId());
        if (category == null) {
            throw new RuntimeException("Категория не найдена");
        }

        Product updatedProduct = productMapper.toProduct(request);
        product.setName(updatedProduct.getName());
        product.setCaloriesPer100g(updatedProduct.getCaloriesPer100g());
        product.setProteinPer100g(updatedProduct.getProteinPer100g());
        product.setFatPer100g(updatedProduct.getFatPer100g());
        product.setCarbsPer100g(updatedProduct.getCarbsPer100g());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) throws RuntimeException {
        Product product = productRepository.findByProductIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Продукт с ID " + id + " не найден или удален"));

        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> searchByName(String name) {
        List<ProductSearchDocument> docs = productSearchRepository.findByNameFuzzy(name);
        return productMapper.toProductResponseList(docs);
    }
}
