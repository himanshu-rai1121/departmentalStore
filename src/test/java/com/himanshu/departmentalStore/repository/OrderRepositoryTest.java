package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DepartmentalStoreApplication.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testSaveOrder() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample order
        Order order = new Order();
        order.setProduct(savedProduct);
        order.setCustomer(savedCustomer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(5);
        Order savedOrder = orderRepository.save(order);

        // Check if the order is saved with an ID
        assertNotNull(savedOrder.getId());
    }

    @Test
    void testFindAllOrders() {
        // Save sample orders
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        Order order1 = new Order();
        order1.setProduct(savedProduct);
        order1.setCustomer(savedCustomer);
        order1.setTimestamp(LocalDateTime.now());
        order1.setQuantity(5);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setProduct(savedProduct);
        order2.setCustomer(savedCustomer);
        order2.setTimestamp(LocalDateTime.now());
        order2.setQuantity(10);
        orderRepository.save(order2);

        // Retrieve all orders
        List<Order> orders = orderRepository.findAll();

        // Check if all orders are retrieved
        assertEquals(2, orders.size());
    }

    @Test
    void testFindById() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample order
        Order order = new Order();
        order.setProduct(savedProduct);
        order.setCustomer(savedCustomer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(5);
        Order savedOrder = orderRepository.save(order);

        // Retrieve the order by ID
        Optional<Order> foundOrderOptional = orderRepository.findById(savedOrder.getId());
        assertTrue(foundOrderOptional.isPresent());
        Order foundOrder = foundOrderOptional.get();

        // Check if the retrieved order matches the saved one
        assertEquals(savedOrder.getId(), foundOrder.getId());
    }

    @Test
    void testUpdateOrder() {
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample order
        Order order = new Order();
        order.setProduct(savedProduct);
        order.setCustomer(savedCustomer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(5);
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
        // Create a sample product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.50));
        product.setCount(100);
        product.setAvailability(true);
        Product savedProduct = productRepository.save(product);

        // Create a sample customer
        Customer customer = new Customer();
        customer.setFullName("John Doe");
        customer.setAddress("123 Main St");
        customer.setContactNumber("1234567890");
        Customer savedCustomer = customerRepository.save(customer);

        // Create a sample order
        Order order = new Order();
        order.setProduct(savedProduct);
        order.setCustomer(savedCustomer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(5);
        Order savedOrder = orderRepository.save(order);

        // Delete the order
        orderRepository.deleteById(savedOrder.getId());

        // Check if the order is deleted
        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }
}
