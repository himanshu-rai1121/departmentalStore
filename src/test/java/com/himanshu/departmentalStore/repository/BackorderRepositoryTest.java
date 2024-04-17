package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class BackorderRepositoryTest {

    @Autowired
    private BackorderRepository backorderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        backorderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testSaveBackorder() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample backorder
        Backorder backorder = new Backorder();
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setTimestamp(LocalDateTime.now());
        backorder.setQuantity(5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Check if the backorder is saved with an ID
        assertNotNull(savedBackorder.getId());
    }

    @Test
    void testFindAllBackorders() {
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        Backorder backorder1 = new Backorder();
        backorder1.setProduct(product);
        backorder1.setCustomer(customer);
        backorder1.setTimestamp(LocalDateTime.now());
        backorder1.setQuantity(5);
        backorderRepository.save(backorder1);

        Backorder backorder2 = new Backorder();
        backorder2.setProduct(product);
        backorder2.setCustomer(customer);
        backorder2.setTimestamp(LocalDateTime.now());
        backorder2.setQuantity(10);
        backorderRepository.save(backorder2);

        // Retrieve all backorders
        List<Backorder> backorders = backorderRepository.findAll();

        // Check if all backorders are retrieved
        assertEquals(2, backorders.size());
    }

    @Test
    void testFindById() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample backorder
        Backorder backorder = new Backorder();
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setTimestamp(LocalDateTime.now());
        backorder.setQuantity(5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Retrieve the backorder by ID
        Optional<Backorder> foundBackorderOptional = backorderRepository.findById(savedBackorder.getId());
        assertTrue(foundBackorderOptional.isPresent());
        Backorder foundBackorder = foundBackorderOptional.get();

        // Check if the retrieved backorder matches the saved one
        assertEquals(savedBackorder.getId(), foundBackorder.getId());
    }

    @Test
    void testUpdateBackorder() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample backorder
        Backorder backorder = new Backorder();
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setTimestamp(LocalDateTime.now());
        backorder.setQuantity(5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Update the backorder's quantity
        savedBackorder.setQuantity(10);

        // Save the updated backorder
        Backorder updatedBackorder = backorderRepository.save(savedBackorder);

        // Retrieve the updated backorder by ID
        Optional<Backorder> foundUpdatedBackorderOptional = backorderRepository.findById(updatedBackorder.getId());
        assertTrue(foundUpdatedBackorderOptional.isPresent());
        Backorder foundUpdatedBackorder = foundUpdatedBackorderOptional.get();

        // Check if the updated backorder matches the changes
        assertEquals(10, foundUpdatedBackorder.getQuantity());
    }

    @Test
    void testDeleteBackorder() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample backorder
        Backorder backorder = new Backorder();
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setTimestamp(LocalDateTime.now());
        backorder.setQuantity(5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Delete the backorder
        backorderRepository.deleteById(savedBackorder.getId());

        // Check if the backorder is deleted
        assertFalse(backorderRepository.existsById(savedBackorder.getId()));
    }
}
