package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setDescription("Sample description");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(100);
        product.setAvailability(true);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Check if the product is saved with an ID
        assertNotNull(savedProduct.getId());
        productRepository.delete(product);
    }

    @Test
    void testFindAllProducts() {
        // Save sample products
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(BigDecimal.valueOf(20.99));
        product1.setExpiry(LocalDate.now().plusMonths(3));
        product1.setCount(50);
        product1.setAvailability(true);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(BigDecimal.valueOf(15.99));
        product2.setExpiry(LocalDate.now().plusMonths(6));
        product2.setCount(75);
        product2.setAvailability(false);
        productRepository.save(product2);

        // Retrieve all products
        List<Product> products = productRepository.findAll();

        // Check if all products are retrieved
        assertEquals(2, products.size());

        productRepository.delete(product1);
        productRepository.delete(product2);
    }

    @Test
    void testFindById() {
        // Save a sample product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(30.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(25);
        product.setAvailability(true);
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

        productRepository.delete(product);
    }

    @Test
    void testDeleteProduct() {
        // Save a sample product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(30.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(25);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Delete the product
        productRepository.deleteById(savedProduct.getId());

        // Check if the product is deleted
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }

    @Test
    void testUpdateProduct() {
        // Save a sample product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(30.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(25);
        product.setAvailability(true);
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

        // Clean up: delete the product
        productRepository.delete(updatedProduct);
    }
}
