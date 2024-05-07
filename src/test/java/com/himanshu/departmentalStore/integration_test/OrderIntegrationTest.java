package com.himanshu.departmentalStore.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.*;
import com.himanshu.departmentalStore.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderIntegrationTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private BackorderRepository backorderRepository;

    private Order order;
    private Customer customer;
    private Product product;
    private Discount discount;


    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
        discountRepository.deleteAll();

        customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        customerRepository.save(customer);
        product = createProductMock("Product 1", "Description 1",
                BigDecimal.valueOf(10.0), LocalDate.now().plusMonths(6), 100, true);
        productRepository.save(product);

        order = new Order();
        order.setProduct(product);
        order.setCustomer(customer);
        order.setQuantity(1);
        order.setTimestamp(LocalDateTime.now());
        order.setAmount(BigDecimal.valueOf(10.0));
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
        discountRepository.deleteAll();
    }

    @Test
    public void getAllOrders() throws Exception {
        orderRepository.save(order);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders")
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
    }

    @Test
    public void getOrderById() throws Exception {
        orderRepository.save(order);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/{id}", order.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(order.getAmount()))
         .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(order.getQuantity()));
    }

    @Test
    public void getNonExistingOrderById() throws Exception {
        orderRepository.save(order);
        Long nonExistingOrderId = order.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/{id}", nonExistingOrderId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Order not found with Id : " + nonExistingOrderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void createOrder() throws Exception {
         OrderRequestBody orderRequestBody = new OrderRequestBody();
         orderRequestBody.setProductId(product.getId());
         orderRequestBody.setCustomerId(customer.getId());
         orderRequestBody.setQuantity(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(order.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(order.getQuantity()));
    }

    @Test
    public void createOrderWithNonExistingCustomer() throws Exception {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId());
        orderRequestBody.setCustomerId(customer.getId() + 100L);
        orderRequestBody.setQuantity(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createOrderWithNonExistingProduct() throws Exception {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId() + 100L);
        orderRequestBody.setCustomerId(customer.getId());
        orderRequestBody.setQuantity(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createOrderWithExtraQuantityThenStock() throws Exception {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId());
        orderRequestBody.setCustomerId(customer.getId());
        orderRequestBody.setQuantity(1000);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders")
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void updateOrder() throws Exception {
        orderRepository.save(order);

        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId());
        orderRequestBody.setCustomerId(customer.getId());
        orderRequestBody.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/orders/{id}", order.getId())
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(order.getAmount().multiply(BigDecimal.valueOf(orderRequestBody.getQuantity()))))// ignoring discount
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(orderRequestBody.getQuantity()));
    }

    @Test
    public void updateNonExistingOrder() throws Exception {
        orderRepository.save(order);
        Long nonExistingOrderId = order.getId() + 100L;

        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId());
        orderRequestBody.setCustomerId(customer.getId());
        orderRequestBody.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/orders/{id}", nonExistingOrderId)
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Order not found with Id : " + nonExistingOrderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void updateOrderWithDifferentCustomer() throws Exception {
        orderRepository.save(order);

        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId());
        orderRequestBody.setCustomerId(customer.getId() + 100L);
        orderRequestBody.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/orders/{id}", order.getId())
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void updateOrderWithDifferentProduct() throws Exception {
        orderRepository.save(order);

        OrderRequestBody orderRequestBody = new OrderRequestBody();
        orderRequestBody.setProductId(product.getId() + 100L);
        orderRequestBody.setCustomerId(customer.getId());
        orderRequestBody.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/orders/{id}", order.getId())
                        .contentType("application/json")
                        .content(asJsonString(orderRequestBody))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void deleteOrder() throws Exception {
        orderRepository.save(order);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/orders/{id}", order.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Resource with ID " + order.getId() + " deleted successfully."));
    }

    @Test
    public void deleteNonExistingOrder() throws Exception {
        orderRepository.save(order);
        Long nonExistingOrderId = order.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/orders/{id}", nonExistingOrderId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Order not found with Id : " + nonExistingOrderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    public static String asJsonString(final Object object) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            final String jsonContent = mapper.writeValueAsString(object);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Customer createMockCustomer(String fullName, String address, String contactNumber) {
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setAddress(address);
        customer.setContactNumber(contactNumber);
        return customer;
    }

    private Product createProductMock(String name, String description, BigDecimal price,
                                      LocalDate expiry, int count, boolean availability) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setExpiry(expiry);
        product.setCount(count);
        product.setAvailability(availability);
        return product;
    }
}
