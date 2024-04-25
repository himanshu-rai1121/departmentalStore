package com.himanshu.departmentalStore.service.negativeTest;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import com.himanshu.departmentalStore.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomerServiceNegativeTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getCustomerById_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(nonExistentId));
    }

    @Test
    void updateCustomer_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        Customer customerToUpdate = new Customer();
        when(customerRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(nonExistentId, customerToUpdate));
    }

    @Test
    void deleteCustomer_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(nonExistentId));

    }

    @Test
    void saveCustomer_NullObject_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> customerService.saveCustomer(null));
    }

    @Test
    void updateCustomer_WithNullObject_ShouldThrowResourceNotFoundException() {
        long customerId = 1L;

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(customerId, null));
    }

    @Test
    void deleteCustomer_WithNullId_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> customerService.deleteCustomer(null));
    }

    @Test
    void deleteCustomer_WithNegativeId_ShouldThrowResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(-1L));
    }
}
