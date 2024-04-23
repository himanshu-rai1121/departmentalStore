package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }
    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testFindAllOrders() {
        // Save sample orders
        Order order1 = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        orderRepository.save(order1);

        Order order2 = createOrderMock(2L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 10);

        orderRepository.save(order2);

        // Retrieve all orders
        List<Order> orders = orderRepository.findAll();

        // Check if all orders are retrieved
        assertEquals(2, orders.size());
    }

    @Test
    void testFindById() {
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        Order savedOrder = orderRepository.save(order);

        // Retrieve the order by ID
        Optional<Order> foundOrderOptional = orderRepository.findById(savedOrder.getId());
        assertTrue(foundOrderOptional.isPresent());
        Order foundOrder = foundOrderOptional.get();

        // Check if the retrieved order matches the saved one
        assertEquals(savedOrder.getId(), foundOrder.getId());
    }

    @Test
    void testSaveOrder() {
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        Order savedOrder = orderRepository.save(order);

        // Check if the order is saved with an ID
        assertNotNull(savedOrder.getId());
    }
    @Test
    void testUpdateOrder() {
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        Order savedOrder = orderRepository.save(order);

        // Update the order's quantity
        savedOrder.setQuantity(10);

        // Save the updated order
        Order updatedOrder = orderRepository.save(savedOrder);

        // Retrieve the updated order by ID
        Optional<Order> foundUpdatedOrderOptional = orderRepository.findById(updatedOrder.getId());
        assertTrue(foundUpdatedOrderOptional.isPresent());
        Order foundUpdatedOrder = foundUpdatedOrderOptional.get();

        // Check if the updated order matches the changes
        assertEquals(10, foundUpdatedOrder.getQuantity());
    }

    @Test
    void testDeleteOrder() {
        Order order = createOrderMock(1L, createProductMock(), createCustomerMock(), LocalDateTime.now(), 5);

        Order savedOrder = orderRepository.save(order);

        // Delete the order
        orderRepository.deleteById(savedOrder.getId());

        // Check if the order is deleted
        assertFalse(orderRepository.existsById(savedOrder.getId()));
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
        return productRepository.save(product);
    }

    private Customer createCustomerMock() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        return customerRepository.save(customer);
    }
}
