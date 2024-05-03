package com.himanshu.departmentalStore.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerIntegrationTest extends AbstractTestContainer{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        customerRepository.deleteAll();

        customer = new Customer();
        customer.setFullName("Himanshu Kumar");
        customer.setAddress("Delhi");
        customer.setContactNumber("1234567890");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        customerRepository.deleteAll();
    }

    @Test
    public void getAllCustomers() throws Exception {
        customerRepository.save(customer);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customers")
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
    }

    @Test
    public void getCustomerById() throws Exception {
        customerRepository.save(customer);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customers/{id}", customer.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Himanshu Kumar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Delhi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactNumber").value("1234567890"));
    }
    @Test
    public void getNonExistingCustomerById() throws Exception {
        customerRepository.save(customer);
        Long customerId = customer.getId() + 100;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customers/{id}", customerId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer not found with Id : "+customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void saveCustomer() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/customers")
                .contentType("application/json")
                .content(asJsonString(customer))
                .accept("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Himanshu Kumar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Delhi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactNumber").value("1234567890"));
    }

    @Test
    public void updateCustomer() throws Exception {
        customerRepository.save(customer);
        customer.setAddress("Gurgaon");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/customers/{id}", customer.getId())
                        .contentType("application/json")
                        .content(asJsonString(customer))
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Himanshu Kumar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Gurgaon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactNumber").value("1234567890"));
    }

    @Test
    public void updateNonExistingCustomer() throws Exception {
        customerRepository.save(customer);
        Long customerId = customer.getId() + 100;

        customer.setAddress("Gurgaon");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/customers/{id}", customerId)
                        .contentType("application/json")
                        .content(asJsonString(customer))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer not found with Id : "+customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void deleteCustomer() throws Exception {
        customerRepository.save(customer);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/customers/{id}", customer.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Resource with ID " + customer.getId() + " deleted successfully."));
    }

    @Test
    public void deleteNonExistingCustomer() throws Exception {
        customerRepository.save(customer);
        Long customerId = customer.getId()+100;
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/customers/{id}", customerId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer not found with Id : "+customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }
    public static String asJsonString(final Object object) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(object);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
