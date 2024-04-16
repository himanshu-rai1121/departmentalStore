package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProducts() {
        // Mocking behavior
        List<Product> products = Arrays.asList(
                createProductMock(1L, "Product 1", "Description 1", BigDecimal.valueOf(10.99),
                        LocalDate.now().plusMonths(6), 100, true),
                createProductMock(2L, "Product 2", "Description 2", BigDecimal.valueOf(20.99),
                        LocalDate.now().plusMonths(3), 50, false)
        );
        when(productService.getAllProducts()).thenReturn(products);

        // Test
        List<Product> result = productController.getAllProducts();

        // Verification
        assertEquals(2, result.size());
    }

    @Test
    void getProductById() {
        // Mocking behavior
        Long productId = 1L;
        Product product = createProductMock(productId, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);
        when(productService.getProductById(productId)).thenReturn(product);

        // Test
        Product result = productController.getProductById(productId);

        // Verification
        assertEquals(productId, result.getId());
    }

    @Test
    void saveProduct() {
        // Mocking behavior
        Product product = createProductMock(1L, "Product 1", "Description 1", BigDecimal.valueOf(10.99),
                LocalDate.now().plusMonths(6), 100, true);
        when(productService.saveProduct(product)).thenReturn(product);

        // Test
        Product result = productController.saveProduct(product);

        // Verification
        assertEquals(product.getId(), result.getId());
    }

    @Test
    void updateProduct() {
        // Mocking behavior
        Long productId = 1L;
        Product product = createProductMock(productId, "Product 1", "Description 1", BigDecimal.valueOf(10.99),
                LocalDate.now().plusMonths(6), 100, true);
        when(productService.updateProduct(productId, product)).thenReturn(product);

        product.setName("Updated Product");
        // Test
        Product result = productController.updateProduct(productId, product);

        // Verification
        assertEquals(productId, result.getId());
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void deleteProduct() {
        // Mocking behavior
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(true);

        // Test
        ResponseEntity<String> result = productController.deleteProduct(productId);

        // Verification
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private Product createProductMock(Long id, String name, String description, BigDecimal price,
                                      LocalDate expiry, int count, boolean availability) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setExpiry(expiry);
        product.setCount(count);
        product.setAvailability(availability);
        return product;
    }
}
