package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class BackorderRepositoryTest {

    @Autowired
    private BackorderRepository backorderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        backorderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        backorderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testFindAllBackorders() {
        Backorder backorder1 = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        backorderRepository.save(backorder1);

        Backorder backorder2 = createBackorderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 10);
        backorderRepository.save(backorder2);

        // Retrieve all backorders
        List<Backorder> backorders = backorderRepository.findAll();

        // Check if all backorders are retrieved
        assertEquals(2, backorders.size());
    }

    @Test
    void testFindById() {
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Retrieve the backorder by ID
        Optional<Backorder> foundBackorderOptional = backorderRepository.findById(savedBackorder.getId());
        assertTrue(foundBackorderOptional.isPresent());
        Backorder foundBackorder = foundBackorderOptional.get();

        // Check if the retrieved backorder matches the saved one
        assertEquals(savedBackorder.getId(), foundBackorder.getId());
    }
    @Test
    void testSaveBackorder() {
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Check if the backorder is saved with an ID
        assertNotNull(savedBackorder.getId());
    }

    @Test
    void testUpdateBackorder() {
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
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
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        Backorder savedBackorder = backorderRepository.save(backorder);

        // Delete the backorder
        backorderRepository.deleteById(savedBackorder.getId());

        // Check if the backorder is deleted
        assertFalse(backorderRepository.existsById(savedBackorder.getId()));
    }

    private Backorder createBackorderMock(Long id, Product product, Customer customer, LocalDateTime timestamp, int quantity) {
        Backorder backorder = new Backorder();
        backorder.setId(id);
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setTimestamp(timestamp);
        backorder.setQuantity(quantity);
        return backorder;
    }

    private Product createProductMock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setDescription("Sample Description");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        return productRepository.save(product);
    }

    private Customer createCustomerMock() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFullName("Himanshu Kumar");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        return customerRepository.save(customer);

    }
}
