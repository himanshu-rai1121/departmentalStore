package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.BackorderService;
import com.himanshu.departmentalStore.service.CustomerService;
import com.himanshu.departmentalStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BackorderControllerTest {

    @Mock
    private BackorderService backorderService;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private BackorderController backorderController;

    private ModelMapper modelMapper = mock(ModelMapper.class);

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
        when(backorderService.getAllBackorders()).thenReturn(backorders);

        // Test
        ResponseEntity<List<Backorder>> result = backorderController.getAllBackorders();

        // Verification
        assertEquals(2, result.getBody().size());
    }

    @Test
    void getBackorderById() {
        // Mocking behavior
        Long backorderId = 1L;
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(backorderService.getBackorderById(backorderId)).thenReturn(backorder);

        // Test
        ResponseEntity<Backorder> result = backorderController.getBackorderById(backorderId);

        // Verification
        assertEquals(backorderId, result.getBody().getId());
    }

    @Test
    void saveBackorder() {
        // Mocking behavior
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        BackOrderRequestBody backorderRequestBody = createBackorderRequestBodyMock();
        when(backorderService.saveBackorder(any(Backorder.class))).thenReturn(backorder);
        when(customerService.getCustomerById(backorderRequestBody.getCustomerId())).thenReturn(backorder.getCustomer());
        when(productService.getProductById(backorderRequestBody.getProductId())).thenReturn(backorder.getProduct());
        when(modelMapper.map(any(), any())).thenReturn(backorder);


        // Test
        ResponseEntity<Backorder> result = backorderController.createBackorder(backorderRequestBody);

        // Verification
        assertEquals(backorder.getId(), result.getBody().getId());
    }


    @Test
    void updateBackorder() {
        // Mocking behavior
        Long backorderId = 1L;
        Backorder backorder = createBackorderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        BackOrderRequestBody backorderRequestBody = createBackorderRequestBodyMock();

        when(backorderService.updateBackorder(eq(backorderId), any(Backorder.class))).thenReturn(backorder);
        when(customerService.getCustomerById(backorderRequestBody.getCustomerId())).thenReturn(backorder.getCustomer());
        when(productService.getProductById(backorderRequestBody.getProductId())).thenReturn(backorder.getProduct());
        when(modelMapper.map(any(), any())).thenReturn(backorder);

        backorderRequestBody.setQuantity(10);
        // Test
        ResponseEntity<Backorder> result = backorderController.updateBackorder(backorderId, backorderRequestBody);

        // Verification
        assertEquals(backorderId, result.getBody().getId());
    }

    @Test
    void deleteBackorder() {
        // Mocking behavior
        Long backorderId = 1L;
        when(backorderService.deleteBackorder(backorderId)).thenReturn(true);

        // Test
        ResponseEntity<String> result = backorderController.deleteBackorder(backorderId);

        // Verification
        assertEquals(HttpStatus.OK, result.getStatusCode());
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
        customer.setFullName("Himanshu Kumar");
        customer.setAddress("Delhi");
        customer.setContactNumber("1234567890");
        return customer;
    }
    private BackOrderRequestBody createBackorderRequestBodyMock() {
        BackOrderRequestBody backorderRequestBody = new BackOrderRequestBody();
        backorderRequestBody.setProductId(1L);
        backorderRequestBody.setCustomerId(1L);
        backorderRequestBody.setQuantity(5);
        return backorderRequestBody;
    }

}
