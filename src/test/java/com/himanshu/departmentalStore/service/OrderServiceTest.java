package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.*;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private BackorderService backorderService;

    @Mock
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllOrders() {
        // Mocking behavior
        List<Order> orders = Arrays.asList(
                createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5),
                createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 10)
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
        Order order = createOrderMock(orderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(),5);
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
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5);
        when(orderRepository.save(order)).thenReturn(order);
        when(discountService.getDiscountById(any())).thenReturn(null);
        when(productService.getProductById(any())).thenReturn(order.getProduct());


        // Test
        Order result = orderService.createOrder(order);

        // Verification
        assertNotNull(result.getId());
    }

    @Test
    void updateOrder() {
        // Mocking behavior
        Long orderId = 1L;
        Order previousOrder = createOrderMock(orderId, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5);

        // Set the quantity to a different value for the updated order
        Order updatedOrder = createOrderMock(orderId, previousOrder.getProduct(), previousOrder.getCustomer(), LocalDateTime.now(), previousOrder.getDiscount(), 10);

        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(previousOrder));
        when(productService.updateProduct(orderId, updatedOrder.getProduct())).thenReturn(updatedOrder.getProduct());
        when(discountService.getDiscountById(any())).thenReturn(previousOrder.getDiscount());

        // Test
        Order result = orderService.updateOrder(orderId, updatedOrder);

        // Verification
        assertEquals(orderId, result.getId());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void deleteOrder() {
        // Mocking behavior
        List<Order> orders = Arrays.asList(
                createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5),
                createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(),10)
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

    private Order createOrderMock(Long id, Product product, Customer customer, LocalDateTime timestamp, Discount discount, int quantity) {
        Order order = new Order();
        order.setId(id);
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(timestamp);
        order.setDiscount(discount);
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
    private Discount createDiscountMock() {
        Discount discount = new Discount();
        discount.setName("50% Discount");
        discount.setValue(new BigDecimal(50.00));
        discount.setStartDateTime(LocalDateTime.now().minusDays(7));
        discount.setEndDateTime(LocalDateTime.now().plusDays(10));
        discount.setDescription("Enjoy 50% discount on all orders.");
        discount.setMinPrice(new BigDecimal(0.00));
        discount.setCouponCode("FLAT50");
        return discount;
    }
}
