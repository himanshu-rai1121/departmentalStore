package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BackorderServiceTest {

    @Mock
    private BackorderRepository backorderRepository;

    @InjectMocks
    private BackorderService backorderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllBackorders() {
        // Mocking behavior
        List<Backorder> backorders = Arrays.asList(
                createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5),
                createBackorderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 10)
        );
        when(backorderRepository.findAll()).thenReturn(backorders);

        // Test
        List<Backorder> result = backorderService.getAllBackorders();

        // Verification
        assertEquals(backorders, result);
    }

    @Test
    void getBackorderById() {
        // Mocking behavior
        Long backorderId = 1L;
        Backorder backorder = createBackorderMock(backorderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(backorderRepository.findById(backorderId)).thenReturn(Optional.of(backorder));

        // Test
        Backorder result = backorderService.getBackorderById(backorderId);

        // Verification
        assertNotNull(result);
        assertEquals(backorderId, result.getId());
    }

    @Test
    void saveBackorder() {
        // Mocking behavior
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(backorderRepository.save(backorder)).thenReturn(backorder);

        // Test
        Backorder result = backorderService.saveBackorder(backorder);

        // Verification
        assertNotNull(result.getId());
    }

    @Test
    void updateBackorder() {
        // Mocking behavior
        Long backorderId = 1L;
        Backorder backorder = createBackorderMock(backorderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(backorderRepository.save(backorder)).thenReturn(backorder);
        when(backorderRepository.existsById(backorderId)).thenReturn(true);

        backorder.setQuantity(10);
        // Test
        Backorder result = backorderService.updateBackorder(backorderId, backorder);

        // Verification
        assertEquals(backorderId, result.getId());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void deleteBackorder() {
        // Mocking behavior
        Long backorderId = 1L;
        Backorder backorder = createBackorderMock(backorderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(backorderRepository.findById(backorderId)).thenReturn(Optional.of(backorder));
        doNothing().when(backorderRepository).deleteById(backorderId);

        // Test
        Boolean result = backorderService.deleteBackorder(backorderId);

        // Verification
        assertTrue(result);
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
        return product;
    }

    private Customer createCustomerMock() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        return customer;
    }
}
