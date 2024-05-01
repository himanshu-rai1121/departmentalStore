package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest(classes = DepartmentalStoreApplication.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BackorderRepository backorderRepository;
    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void testFindAllProducts() {
        // Save sample products
        Product product1 = createProductMock(1L, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);

        productRepository.save(product1);

        Product product2 = createProductMock(2L, "Product 2", "Description 2", BigDecimal.valueOf(20.99),
                LocalDate.now().plusMonths(3), 50, false);

        productRepository.save(product2);

        // Retrieve all products
        List<Product> products = productRepository.findAll();

        // Check if all products are retrieved
        assertEquals(2, products.size());
    }

    @Test
    void testFindById() {
        // Save a sample product
        Product product = createProductMock(1L, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);

        Product savedProduct = productRepository.save(product);

        // Retrieve the product by ID
        Optional<Product> foundProductOptional = productRepository.findById(savedProduct.getId());
        assertTrue(foundProductOptional.isPresent());
        Product foundProduct = foundProductOptional.get();

        // Check if the retrieved product matches the saved one
        assertEquals(savedProduct.getId(), foundProduct.getId());
        assertEquals(savedProduct.getName(), foundProduct.getName());
        assertEquals(savedProduct.getDescription(), foundProduct.getDescription());
        assertEquals(savedProduct.getPrice(), foundProduct.getPrice());
        assertEquals(savedProduct.getExpiry(), foundProduct.getExpiry());
        assertEquals(savedProduct.getCount(), foundProduct.getCount());
        assertEquals(savedProduct.isAvailability(), foundProduct.isAvailability());

    }
    @Test
    void testSaveProduct() {
        // Create a sample product
        Product product = createProductMock(1L, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Check if the product is saved with an ID
        assertNotNull(savedProduct.getId());
    }



    @Test
    void testUpdateProduct() {
        // Save a sample product
        Product product = createProductMock(1L, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);
        Product savedProduct = productRepository.save(product);

        // Update the product's information
        savedProduct.setName("Updated Product");
        savedProduct.setPrice(BigDecimal.valueOf(25.99));
        savedProduct.setCount(50);

        // Save the updated product
        Product updatedProduct = productRepository.save(savedProduct);

        // Retrieve the updated product by ID
        Optional<Product> foundUpdatedProductOptional = productRepository.findById(updatedProduct.getId());
        assertTrue(foundUpdatedProductOptional.isPresent());
        Product foundUpdatedProduct = foundUpdatedProductOptional.get();

        // Check if the updated product matches the changes
        assertEquals("Updated Product", foundUpdatedProduct.getName());
        assertEquals(BigDecimal.valueOf(25.99), foundUpdatedProduct.getPrice());
        assertEquals(50, foundUpdatedProduct.getCount());

    }
    @Test
    void testDeleteProduct() {
        // Save a sample product
        Product product = createProductMock(1L, "Product 1", "Description 1",
                BigDecimal.valueOf(10.99), LocalDate.now().plusMonths(6), 100, true);
        Product savedProduct = productRepository.save(product);

        // Delete the product
        productRepository.deleteById(savedProduct.getId());

        // Check if the product is deleted
        assertFalse(productRepository.existsById(savedProduct.getId()));
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
