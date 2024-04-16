package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllCustomers() {
        // Mocking behavior
        List<Customer> customers = Arrays.asList(
                createCustomerMock(1L, "John Doe", "123 Main St", "1234567890"),
                createCustomerMock(2L, "Jane Smith", "456 Elm St", "9876543210")
        );
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Test
        List<Customer> result = customerController.getAllCustomers().getBody();

        // Verification
        assertEquals(2, result.size());
    }

    @Test
    void getCustomerById() {
        // Mocking behavior
        Long customerId = 1L;
        Customer customer = createCustomerMock(customerId, "John Doe", "123 Main St", "1234567890");
        when(customerService.getCustomerById(customerId)).thenReturn(customer);

        // Test
        Customer result = customerController.getCustomerById(customerId).getBody();

        // Verification
        assertEquals(customerId, result.getId());
    }

    @Test
    void saveCustomer() {
        // Mocking behavior
        Customer customer = createCustomerMock(1L, "John Doe", "123 Main St", "1234567890");
        when(customerService.saveCustomer(customer)).thenReturn(customer);

        // Test
        Customer result = customerController.saveCustomer(customer).getBody();

        // Verification
        assertEquals(customer.getId(), result.getId());
    }

    @Test
    void updateCustomer() {
        // Mocking behavior
        Long customerId = 1L;
        Customer customer = createCustomerMock(customerId, "John Doe", "123 Main St", "1234567890");
        when(customerService.updateCustomer(customerId, customer)).thenReturn(customer);

        customer.setFullName("John");
        // Test
        Customer result = customerController.updateCustomer(customerId, customer).getBody();

        // Verification
        assertEquals(customerId, result.getId());
    }

    @Test
    void deleteCustomer() {
        // Mocking behavior
        Long customerId = 1L;
        when(customerService.deleteCustomer(customerId)).thenReturn(true);

        // Test
        ResponseEntity<String> result = customerController.deleteCustomer(customerId);

        // Verification
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private Customer createCustomerMock(Long id, String fullName, String address, String contactNumber) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFullName(fullName);
        customer.setAddress(address);
        customer.setContactNumber(contactNumber);
        return customer;
    }
}
