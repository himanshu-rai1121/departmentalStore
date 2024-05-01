package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BackorderRepository backorderRepository;
    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        // Create a sample customer
        Customer customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        // Check if the customer is saved with an ID
        assertNotNull(savedCustomer.getId());
    }

    @Test
    void testFindAllCustomers() {
        // Save sample customers
        Customer customer1 = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        customerRepository.save(customer1);

        Customer customer2 = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        customerRepository.save(customer2);

        // Retrieve all customers
        List<Customer> customers = customerRepository.findAll();

        // Check if all customers are retrieved
        assertEquals(2, customers.size());

    }

    @Test
    void testFindById() {
        // Save a sample customer
        Customer customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Retrieve the customer by ID
        Customer foundCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        // Check if the retrieved customer matches the saved one
        assertNotNull(foundCustomer);
        assertEquals(savedCustomer.getId(), foundCustomer.getId());
        assertEquals(savedCustomer.getFullName(), foundCustomer.getFullName());
        assertEquals(savedCustomer.getAddress(), foundCustomer.getAddress());
        assertEquals(savedCustomer.getContactNumber(), foundCustomer.getContactNumber());

    }

    @Test
    void testDeleteCustomer() {
        // Save a sample customer
        Customer customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Delete the customer
        customerRepository.deleteById(savedCustomer.getId());

        // Check if the customer is deleted
        assertFalse(customerRepository.existsById(savedCustomer.getId()));
    }

    @Test
    void testUpdateCustomer() {
        // Save a sample customer
        Customer customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Update the customer's information
        savedCustomer.setFullName("Updated Name");
        savedCustomer.setAddress("Gurgaon");
        savedCustomer.setContactNumber("9876543210");

        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        // Retrieve the updated customer by ID
        Optional<Customer> foundUpdatedCustomerOptional = customerRepository.findById(updatedCustomer.getId());
        assertTrue(foundUpdatedCustomerOptional.isPresent());
        Customer foundUpdatedCustomer = foundUpdatedCustomerOptional.get();

        // Check if the updated customer matches the changes
        assertEquals("Updated Name", foundUpdatedCustomer.getFullName());
        assertEquals("Gurgaon", foundUpdatedCustomer.getAddress());
        assertEquals("9876543210", foundUpdatedCustomer.getContactNumber());

    }
    private Customer createMockCustomer(String fullName, String address, String contactNumber) {
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setAddress(address);
        customer.setContactNumber(contactNumber);
        Customer savedCustomer = customerRepository.save(customer);
        return customer;
    }
}
