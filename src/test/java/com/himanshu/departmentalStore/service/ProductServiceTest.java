package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
        when(productRepository.findAll()).thenReturn(products);

        // Test
        List<Product> result = productService.getAllProducts();

        // Verification
        assertEquals(products, result);
    }

    @Test
    void getProductById() {
        // Mocking behavior
        Long productId = 1L;
        Product product = createProductMock(productId, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Test
        Product result = productService.getProductById(productId);

        // Verification
        assertNotNull(result);
        assertEquals(productId, result.getId());
    }

    @Test
    void saveProduct() {
        // Mocking behavior
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(100);
        product.setAvailability(true);
        when(productRepository.save(product)).thenReturn(product);

        // Test
        Product result = productService.saveProduct(product);

        // Verification
        assertNotNull(result.getId());
    }

    @Test
    void updateProduct() {
        // Mocking behavior
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(100);
        product.setAvailability(true);
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.existsById(productId)).thenReturn(true);

        product.setName("Updated Product");
        // Test
        Product result = productService.updateProduct(productId, product);

        // Verification
        assertEquals(productId, result.getId());
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void deleteProduct() {
        // Mocking behavior
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).deleteById(productId);

        // Test
        Boolean result = productService.deleteProduct(productId);

        // Verification
        assertTrue(result);
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
