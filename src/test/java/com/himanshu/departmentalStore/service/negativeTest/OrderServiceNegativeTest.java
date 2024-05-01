package com.himanshu.departmentalStore.service.negativeTest;

import com.himanshu.departmentalStore.exception.CustomException;
import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.*;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import com.himanshu.departmentalStore.repository.OrderRepository;
import com.himanshu.departmentalStore.service.BackorderService;
import com.himanshu.departmentalStore.service.OrderService;
import com.himanshu.departmentalStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


class OrderServiceNegativeTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private BackorderService backorderService;

    @Mock
    private BackorderRepository backorderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createOrder_ProductQuantityLess_ShouldThrowCustomException() {
        Product product = new Product();
        product.setId(1L);
        product.setCount(1);



        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5);

        Backorder backorder = new Backorder();
        backorder.setId(1L);
        backorder.setQuantity(order.getQuantity());
        backorder.setCustomer(order.getCustomer());
        backorder.setProduct(order.getProduct());
        backorder.setTimestamp(order.getTimestamp());

        when(productService.getProductById(1L)).thenReturn(product);
        when(backorderService.saveBackorder(any(Backorder.class))).thenReturn(backorder);
        when(backorderRepository.save(any(Backorder.class))).thenReturn(backorder);

        assertThrows(CustomException.class, () -> orderService.createOrder(order));
    }

    @Test
    void updateOrder_OrderNotFound_ShouldThrowResourceNotFoundException() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(1L, order));
    }

    @Test
    void updateOrder_DifferentProduct_ShouldThrowCustomException() {

        Order existingOrder = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5);

        Order updatedOrder = createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), createDiscountMock(), 5);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        assertThrows(CustomException.class, () -> orderService.updateOrder(1L, updatedOrder));
    }

    @Test
    void deleteOrder_OrderNotFound_ShouldThrowResourceNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(1L));

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
        customer.setFullName("Himanshu Kumar");
        customer.setAddress("Delhi");
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
