package ru.naumen.calorietracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.naumen.calorietracker.dto.ProductCreateRequest;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.dto.ProductUpdateRequest;
import ru.naumen.calorietracker.model.User;
import ru.naumen.calorietracker.service.ProductService;
import ru.naumen.calorietracker.util.AuthUtils;

import java.security.Principal;
import java.util.List;

/**
 * REST-контроллер для управления продуктами.
 * Позволяет создавать, получать, обновлять, удалять продукты и искать продукты по имени.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;
    private final AuthUtils authUtils;

    /**
     * Добавить новый продукт.
     *
     * @param request данные для создания продукта
     * @param principal текущий аутентифицированный пользователь
     * @return созданный продукт или ошибка
     */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreateRequest request, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            ProductResponse response = productService.createProduct(request, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Получить список всех продуктов.
     *
     * @param principal текущий аутентифицированный пользователь
     * @return список продуктов или ошибка
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(Principal principal) {
        try {
            authUtils.checkAuthentication(principal);
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Получить продукт по ID.
     *
     * @param id ID продукта
     * @param principal текущий аутентифицированный пользователь
     * @return продукт или ошибка
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id, Principal principal) {
        try {
            authUtils.checkAuthentication(principal);
            ProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Обновить продукт по ID.
     *
     * @param id ID продукта
     * @param request данные для обновления продукта
     * @param principal текущий аутентифицированный пользователь
     * @return обновлённый продукт или ошибка
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductUpdateRequest request, Principal principal) {
        try {
            User user = authUtils.getAuthenticatedUser(principal);
            ProductResponse response = productService.updateProduct(id, request, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удалить продукт по ID (только для пользователей с ролью ADMIN).
     *
     * @param id ID продукта
     * @param principal текущий аутентифицированный пользователь
     * @return сообщение об успехе или ошибка
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, Principal principal) {
        try {
            authUtils.checkAuthentication(principal);
            productService.deleteProduct(id);
            return ResponseEntity.ok("Продукт успешно удален");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Поиск продуктов по части имени.
     *
     * @param name часть названия продукта для поиска
     * @return список продуктов, удовлетворяющих условию поиска
     */
    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String name) {
        return productService.searchByName(name);
    }

}