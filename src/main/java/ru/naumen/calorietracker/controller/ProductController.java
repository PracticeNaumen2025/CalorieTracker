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
import ru.naumen.calorietracker.repository.UserRepository;
import ru.naumen.calorietracker.service.ProductService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreateRequest request, Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            ProductResponse response = productService.createProduct(request, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(Principal principal) {
        try {
            checkAuthentication(principal);
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id, Principal principal) {
        try {
            checkAuthentication(principal);
            ProductResponse product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductUpdateRequest request, Principal principal) {
        try {
            User user = getAuthenticatedUser(principal);
            ProductResponse response = productService.updateProduct(id, request, user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, Principal principal) {
        try {
            checkAuthentication(principal);
            productService.deleteProduct(id);
            return ResponseEntity.ok("Продукт успешно удален");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String name) {
        return productService.searchByName(name);
    }

    private void checkAuthentication(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }
    }

    private User getAuthenticatedUser(Principal principal) {
        checkAuthentication(principal);
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}