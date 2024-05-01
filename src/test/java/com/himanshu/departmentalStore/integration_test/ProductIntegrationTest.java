package com.himanshu.departmentalStore.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.himanshu.departmentalStore.model.Product;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        productRepository.deleteAll();

        product = new Product();
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setExpiry(LocalDate.now().plusMonths(6));
        product.setCount(100);
        product.setAvailability(true);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        productRepository.deleteAll();
    }

    @Test
    public void getAllProducts() throws Exception {
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products")
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
    }

    @Test
    public void getProductById() throws Exception {
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/{id}", product.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("10.99"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiry").value(product.getExpiry().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availability").value(true));
    }

    @Test
    public void getNonExistingProductById() throws Exception {
        productRepository.save(product);

        Long productId = product.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/{id}", productId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found with Id : " + productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void saveProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/products")
                        .contentType("application/json")
                        .content(asJsonString(product))
                        .accept("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("10.99"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiry").value(product.getExpiry().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availability").value(true));
    }

    @Test
    public void updateProduct() throws Exception {
        productRepository.save(product);

        product.setName("Updated Product");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/products/{id}", product.getId())
                        .contentType("application/json")
                        .content(asJsonString(product))
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Product"));
    }

    @Test
    public void updateNonExistingProduct() throws Exception {
        productRepository.save(product);

        Long productId = product.getId() + 100L;
        product.setName("Updated Product");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/products/{id}", productId)
                        .contentType("application/json")
                        .content(asJsonString(product))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found with Id : " + productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void deleteProduct() throws Exception {
        productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/products/{id}", product.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Resource with ID " + product.getId() + " deleted successfully."));
    }

    @Test
    public void deleteNonExistingProduct() throws Exception {
        productRepository.save(product);

        Long productId = product.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/products/{id}", productId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product not found with Id : " + productId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    public static String asJsonString(final Object object) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            final String jsonContent = mapper.writeValueAsString(object);
            return jsonContent;
//
//            final ObjectMapper mapper = new ObjectMapper();
//            // Register JavaTimeModule to handle java.time.LocalDate
//            mapper.registerModule(new JavaTimeModule());
//            final String jsonContent = mapper.writeValueAsString(object);
//            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
