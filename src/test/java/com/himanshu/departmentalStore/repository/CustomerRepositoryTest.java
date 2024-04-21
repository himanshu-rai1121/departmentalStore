package com.himanshu.departmentalStore.repository;


import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        // Check if the customer is saved with an ID
        assertNotNull(savedCustomer.getId());



        customerRepository.delete(customer);
    }

    @Test
    void testFindAllCustomers() {
        // Save sample customers
        Customer customer1 = new Customer();
        customer1.setFullName("John Doe");
        customer1.setAddress("123 Main St");
        customer1.setContactNumber("1234567890");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setFullName("Jane Smith");
        customer2.setAddress("456 Elm St");
        customer2.setContactNumber("9876543210");
        customerRepository.save(customer2);

        // Retrieve all customers
        List<Customer> customers = customerRepository.findAll();

        // Check if all customers are retrieved
        assertEquals(2, customers.size());

        customerRepository.delete(customer1);
        customerRepository.delete(customer2);
    }

    @Test
    void testFindById() {
        // Save a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Retrieve the customer by ID
        Customer foundCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);

        // Check if the retrieved customer matches the saved one
        assertNotNull(foundCustomer);
        assertEquals(savedCustomer.getId(), foundCustomer.getId());
        assertEquals(savedCustomer.getFullName(), foundCustomer.getFullName());
        assertEquals(savedCustomer.getAddress(), foundCustomer.getAddress());
        assertEquals(savedCustomer.getContactNumber(), foundCustomer.getContactNumber());

        customerRepository.delete(customer);
    }

    @Test
    void testDeleteCustomer() {
        // Save a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Delete the customer
        customerRepository.deleteById(savedCustomer.getId());

        // Check if the customer is deleted
        assertFalse(customerRepository.existsById(savedCustomer.getId()));
    }

    @Test
    void testUpdateCustomer() {
        // Save a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Update the customer's information
        savedCustomer.setFullName("Updated Name");
        savedCustomer.setAddress("456 Elm St");
        savedCustomer.setContactNumber("9876543210");

        // Save the updated customer
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        // Retrieve the updated customer by ID
        Optional<Customer> foundUpdatedCustomerOptional = customerRepository.findById(updatedCustomer.getId());
        assertTrue(foundUpdatedCustomerOptional.isPresent());
        Customer foundUpdatedCustomer = foundUpdatedCustomerOptional.get();

        // Check if the updated customer matches the changes
        assertEquals("Updated Name", foundUpdatedCustomer.getFullName());
        assertEquals("456 Elm St", foundUpdatedCustomer.getAddress());
        assertEquals("9876543210", foundUpdatedCustomer.getContactNumber());

        // Clean up: delete the customer
        customerRepository.delete(updatedCustomer);
    }
}
