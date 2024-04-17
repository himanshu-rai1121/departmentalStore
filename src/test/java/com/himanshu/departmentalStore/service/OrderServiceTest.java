package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.OrderRepository;
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

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private BackorderService backorderService;

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
        when(orderRepository.findAll()).thenReturn(orders);

        // Test
        List<Order> result = orderService.getAllOrders();

        // Verification
        assertEquals(orders, result);
    }

    @Test
    void getOrderById() {
        // Mocking behavior
        Long orderId = 1L;
        Order order = createOrderMock(orderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Test
        Order result = orderService.getOrderById(orderId);

        // Verification
        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    void saveOrder() {
        // Mocking behavior
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(orderRepository.save(order)).thenReturn(order);

        // Test
        Order result = orderService.createOrder(order);

        // Verification
        assertNotNull(result.getId());
    }

    @Test
    void updateOrder() {
        // Mocking behavior
        Long orderId = 1L;
        Order order = createOrderMock(orderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);
        when(orderRepository.save(order)).thenReturn(order);

        order.setQuantity(10);
        // Test
        Order result = orderService.updateOrder(orderId, order);

        // Verification
        assertEquals(orderId, result.getId());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void deleteOrder() {
        // Mocking behavior
        List<Order> orders = Arrays.asList(
                createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5),
                createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 10)
        );
        Long orderId = 1L;
        Order fetchedOrder = orders
                .stream()
                .filter(order -> order.getId().equals(orderId)).findFirst().get();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(fetchedOrder));
        doNothing().when(orderRepository).deleteById(orderId);


        Product product = fetchedOrder.getProduct();
        product.setCount(product.getCount()+fetchedOrder.getQuantity());

        when(productService.updateProduct(product.getId(), product)).thenReturn(product);
        when(backorderService.getAllBackordersByProductId(product.getId())).thenReturn(new ArrayList<>());



        // Test
        Boolean result = orderService.deleteOrder(orderId);

        // Verification
        assertTrue(result);
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
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        return customer;
    }
}
