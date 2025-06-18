package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.naumen.calorietracker.dto.ProductCreateRequest;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.dto.ProductUpdateRequest;
import ru.naumen.calorietracker.mapper.ProductMapper;
import ru.naumen.calorietracker.model.Category;
import ru.naumen.calorietracker.model.Product;
import ru.naumen.calorietracker.model.Role;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.repository.ProductRepository;
import ru.naumen.calorietracker.service.CategoryService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private User user;
    private User admin;
    private User anotherUser;
    private Category category;
    private Product product;
    private ProductResponse productResponse;
    private ProductCreateRequest createRequest;
    private ProductUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Настройка ролей
        Role userRole = new Role();
        userRole.setRoleId(1);
        userRole.setRoleName("USER");

        Role adminRole = new Role();
        adminRole.setRoleId(2);
        adminRole.setRoleName("ADMIN");

        // Настройка обычного пользователя
        user = new User();
        user.setUserId(1);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        // Настройка администратора
        admin = new User();
        admin.setUserId(2);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        admin.setRoles(adminRoles);

        // Настройка другого пользователя (для теста прав)
        anotherUser = new User();
        anotherUser.setUserId(3);
        Set<Role> anotherUserRoles = new HashSet<>();
        anotherUserRoles.add(userRole);
        anotherUser.setRoles(anotherUserRoles);

        // Настройка категории
        category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Фрукты");

        // Настройка продукта
        product = new Product();
        product.setProductId(1);
        product.setName("Яблоко");
        product.setCaloriesPer100g(new BigDecimal("52.0"));
        product.setProteinPer100g(new BigDecimal("0.3"));
        product.setFatPer100g(new BigDecimal("0.2"));
        product.setCarbsPer100g(new BigDecimal("13.8"));
        product.setCategory(category);
        product.setUser(user);
        product.setIsDeleted(false);

        // Настройка ответа
        productResponse = new ProductResponse(
                1,
                "Яблоко",
                new BigDecimal("52.0"),
                new BigDecimal("0.3"),
                new BigDecimal("0.2"),
                new BigDecimal("13.8"),
                1,
                "Фрукты"
        );

        // Настройка запроса на создание
        createRequest = new ProductCreateRequest();
        createRequest.setName("Яблоко");
        createRequest.setCaloriesPer100g(new BigDecimal("52.0"));
        createRequest.setProteinPer100g(new BigDecimal("0.3"));
        createRequest.setFatPer100g(new BigDecimal("0.2"));
        createRequest.setCarbsPer100g(new BigDecimal("13.8"));
        createRequest.setCategoryId(1);

        // Настройка запроса на обновление
        updateRequest = new ProductUpdateRequest();
        updateRequest.setName("Яблоко зеленое");
        updateRequest.setCaloriesPer100g(new BigDecimal("48.0"));
        updateRequest.setProteinPer100g(new BigDecimal("0.4"));
        updateRequest.setFatPer100g(new BigDecimal("0.1"));
        updateRequest.setCarbsPer100g(new BigDecimal("12.5"));
        updateRequest.setCategoryId(1);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        when(productRepository.findAllByIsDeletedFalse()).thenReturn(List.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productResponse, result.getFirst());
        verify(productRepository).findAllByIsDeletedFalse();
        verify(productMapper).toProductResponse(product);
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verify(productMapper).toProductResponse(product);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductById(1));

        assertEquals("Продукт с ID 1 не найден или удален", exception.getMessage());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verifyNoInteractions(productMapper);
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenCategoryExists() {
        when(categoryService.getCategoryById(1)).thenReturn(category);
        when(productMapper.toProduct(createRequest)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.createProduct(createRequest, user);

        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(categoryService).getCategoryById(1);
        verify(productMapper).toProduct(createRequest);
        verify(productRepository).save(product);
        verify(productMapper).toProductResponse(product);
        assertEquals(category, product.getCategory());
        assertEquals(user, product.getUser());
    }

    @Test
    void createProduct_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryService.getCategoryById(1)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.createProduct(createRequest, user));

        assertEquals("Категория не найдена", exception.getMessage());
        verify(categoryService).getCategoryById(1);
        verifyNoInteractions(productMapper, productRepository);
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenUserIsOwner() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(1)).thenReturn(category);
        Product updatedProduct = new Product();
        updatedProduct.setName("Яблоко зеленое");
        updatedProduct.setCaloriesPer100g(new BigDecimal("48.0"));
        updatedProduct.setProteinPer100g(new BigDecimal("0.4"));
        updatedProduct.setFatPer100g(new BigDecimal("0.1"));
        updatedProduct.setCarbsPer100g(new BigDecimal("12.5"));
        when(productMapper.toProduct(updateRequest)).thenReturn(updatedProduct);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.updateProduct(1, updateRequest, user);

        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verify(categoryService).getCategoryById(1);
        verify(productMapper).toProduct(updateRequest);
        verify(productRepository).save(product);
        verify(productMapper).toProductResponse(product);
        assertEquals("Яблоко зеленое", product.getName());
        assertEquals(category, product.getCategory());
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenUserIsAdmin() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(1)).thenReturn(category);
        Product updatedProduct = new Product();
        updatedProduct.setName("Яблоко зеленое");
        updatedProduct.setCaloriesPer100g(new BigDecimal("48.0"));
        updatedProduct.setProteinPer100g(new BigDecimal("0.4"));
        updatedProduct.setFatPer100g(new BigDecimal("0.1"));
        updatedProduct.setCarbsPer100g(new BigDecimal("12.5"));
        when(productMapper.toProduct(updateRequest)).thenReturn(updatedProduct);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.updateProduct(1, updateRequest, admin);

        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verify(categoryService).getCategoryById(1);
        verify(productMapper).toProduct(updateRequest);
        verify(productRepository).save(product);
        verify(productMapper).toProductResponse(product);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenUserNotAuthorized() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, updateRequest, anotherUser));

        assertEquals("Нет прав для редактирования продукта", exception.getMessage());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verifyNoInteractions(categoryService, productMapper);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, updateRequest, user));

        assertEquals("Продукт с ID 1 не найден или удален", exception.getMessage());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verifyNoInteractions(categoryService, productMapper);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenCategoryNotFound() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(1)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.updateProduct(1, updateRequest, user));

        assertEquals("Категория не найдена", exception.getMessage());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verify(categoryService).getCategoryById(1);
        verifyNoInteractions(productMapper);
    }

    @Test
    void deleteProduct_ShouldMarkProductAsDeleted_WhenProductExists() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.deleteProduct(1);

        assertTrue(product.getIsDeleted());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findByProductIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct(1));

        assertEquals("Продукт с ID 1 не найден или удален", exception.getMessage());
        verify(productRepository).findByProductIdAndIsDeletedFalse(1);
        verifyNoMoreInteractions(productRepository);
    }
}