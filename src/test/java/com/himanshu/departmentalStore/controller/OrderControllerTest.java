package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.CustomerService;
import com.himanshu.departmentalStore.service.DiscountService;
import com.himanshu.departmentalStore.service.OrderService;
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
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @Mock
    private CustomerService customerService;
    @Mock
    private DiscountService discountService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private OrderController orderController;
    private ModelMapper modelMapper = mock(ModelMapper.class);
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllOrders() {
        // Mocking behavior
        List<Order> orders = Arrays.asList(
                createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5),
                createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 10)
        );
        when(orderService.getAllOrders()).thenReturn(orders);

        // Test
        ResponseEntity<List<Order>> result = orderController.getAllOrders();

        // Verification
        assertEquals(2, result.getBody().size());
    }

    @Test
    void getOrderById() {
        // Mocking behavior
        Long orderId = 1L;
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // Test
        ResponseEntity<Order> result = orderController.getOrderById(orderId);

        // Verification
        assertEquals(orderId, result.getBody().getId());
    }

    @Test
    void saveOrder() {
        // Mocking behavior
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        OrderRequestBody orderRequestBody = createOrderRequestBodyMock();
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(customerService.getCustomerById(orderRequestBody.getCustomerId())).thenReturn(order.getCustomer());
        when(discountService.getDiscountById(orderRequestBody.getDiscountId())).thenReturn(order.getDiscount());
        when(productService.getProductById(orderRequestBody.getProductId())).thenReturn(order.getProduct());
        when(modelMapper.map(any(), any())).thenReturn(order);

        // Test
        ResponseEntity<Order> result = orderController.createOrder(orderRequestBody);

        // Verification
        System.out.println(order);
        System.out.println(result);
        assertEquals(order.getId(), result.getBody().getId());
    }


    @Test
    void updateOrder() {
        // Mocking behavior
        Long orderId = 1L;
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        OrderRequestBody orderRequestBody = createOrderRequestBodyMock();

        when(orderService.updateOrder(eq(orderId), any(Order.class))).thenReturn(order);
        when(modelMapper.map(any(), any())).thenReturn(order);
        orderRequestBody.setQuantity(10);
        // Test
        ResponseEntity<Order> result = orderController.updateOrder(orderId, orderRequestBody);

        // Verification
        assertEquals(orderId, result.getBody().getId());
    }

    @Test
    void deleteOrder() {
        // Mocking behavior
        Long orderId = 1L;
        when(orderService.deleteOrder(orderId)).thenReturn(true);

        // Test
        ResponseEntity<String> result = orderController.deleteOrder(orderId);

        // Verification
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    private Order createOrderMock(Long id, Product product, Customer customer, LocalDateTime timestamp, int quantity) {
        Order order = new Order();
        order.setId(id);
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(timestamp);
        order.setQuantity(quantity);
        return order;
    }

    private Product createProductMock() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
        product.setDescription("Sample Description");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setExpiry(null);
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
    private OrderRequestBody createOrderRequestBodyMock() {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(1L);
        orderRequestBody.setCustomerId(1L);
        orderRequestBody.setDiscountId(1L);
        orderRequestBody.setQuantity(5);
        return orderRequestBody;
    }

}
