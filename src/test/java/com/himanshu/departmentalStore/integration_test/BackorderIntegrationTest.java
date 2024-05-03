package com.himanshu.departmentalStore.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import com.himanshu.departmentalStore.repository.ProductRepository;
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
public class BackorderIntegrationTest extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BackorderRepository backorderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    private Backorder backorder;
    private Product product;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();

        customer = createMockCustomer("Himanshu Kumar", "Delhi", "1234567890");
        customerRepository.save(customer);
        product = createProductMock("Product 1", "Description 1",
                BigDecimal.valueOf(10.0), LocalDate.now().plusMonths(6), 100, true);
        productRepository.save(product);

        backorder = new Backorder();
        backorder.setProduct(product);
        backorder.setCustomer(customer);
        backorder.setQuantity(100);
        backorder.setTimestamp(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        backorderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void getAllBackorders() throws Exception {
        backorderRepository.save(backorder);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/backorders")
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
    }

    @Test
    public void getBackorderById() throws Exception {
        backorderRepository.save(backorder);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/backorders/{id}", backorder.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(100));
    }

    @Test
    public void getNonExistingBackorderById() throws Exception {
        backorderRepository.save(backorder);
        Long backorderId = backorder.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/backorders/{id}", backorderId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Backorder not found with Id : " + backorderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void createBackorder() throws Exception {
        BackOrderRequestBody requestBody = new BackOrderRequestBody();
        requestBody.setProductId(product.getId());
        requestBody.setQuantity(100);
        requestBody.setCustomerId(customer.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/backorders")
                        .contentType("application/json")
                        .content(asJsonString(requestBody))
                        .accept("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(100));
    }

    @Test
    public void updateBackorder() throws Exception {
        backorderRepository.save(backorder);

        BackOrderRequestBody requestBody = new BackOrderRequestBody();
        requestBody.setProductId(product.getId());
        requestBody.setQuantity(200);
        requestBody.setCustomerId(customer.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/backorders/{id}", backorder.getId())
                        .contentType("application/json")
                        .content(asJsonString(requestBody))
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id").value(product.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(200));
    }

    @Test
    public void updateNonExistingBackorder() throws Exception {
        backorderRepository.save(backorder);
        Long backorderId = backorder.getId() + 100L;

        backorder.setQuantity(200);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/backorders/{id}", backorderId)
                        .contentType("application/json")
                        .content(asJsonString(backorder))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Backorder not found with Id : " + backorderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void deleteBackorder() throws Exception {
        backorderRepository.save(backorder);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/backorders/{id}", backorder.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Resource with ID " + backorder.getId() + " deleted successfully."));
    }

    @Test
    public void deleteNonExistingBackorder() throws Exception {
        backorderRepository.save(backorder);
        Long backorderId = backorder.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/backorders/{id}", backorderId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Backorder not found with Id : " + backorderId))
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

    private Product createProductMock( String name, String description, BigDecimal price,
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
