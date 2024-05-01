package com.himanshu.departmentalStore.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import com.himanshu.departmentalStore.util.CustomDateTimeUtils;
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
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DiscountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DiscountRepository discountRepository;

    private Discount discount;

    @Autowired
    private CustomDateTimeUtils customDateTimeUtils;

    @BeforeEach
    void setUp() {
        discountRepository.deleteAll();

        discount = new Discount();
        discount.setName("Discount 1");
        discount.setDescription("Description 1");
        discount.setValue(BigDecimal.valueOf(50.00));
        discount.setStartDateTime(LocalDateTime.now().minusDays(1));
        discount.setEndDateTime(LocalDateTime.now().plusDays(7));
        discount.setMinPrice(BigDecimal.valueOf(100.0));
        discount.setCouponCode("FLAT50");
    }

    @AfterEach
    void tearDown() {
        discountRepository.deleteAll();
    }

    @Test
    public void getAllDiscounts() throws Exception {
        discountRepository.save(discount);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/discounts")
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
    }

    @Test
    public void getDiscountById() throws Exception {
        discountRepository.save(discount);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/discounts/{id}", discount.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Discount 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value("50.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minPrice").value("100.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.couponCode").value("FLAT50"));
    }

    @Test
    public void getNonExistingDiscountById() throws Exception {
        discountRepository.save(discount);

        Long discountId = discount.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/discounts/{id}", discountId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Discount not found with Id : " + discountId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void saveDiscount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/discounts")
                        .contentType("application/json")
                        .content(asJsonString(discount))
                        .accept("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Discount 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value("50.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minPrice").value("100.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.couponCode").value("FLAT50"));
    }

    @Test
    public void updateDiscount() throws Exception {
        discountRepository.save(discount);

        discount.setName("Updated Discount");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/discounts/{id}", discount.getId())
                        .contentType("application/json")
                        .content(asJsonString(discount))
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Discount"));
    }

    @Test
    public void updateNonExistingDiscount() throws Exception {
        discountRepository.save(discount);

        Long discountId = discount.getId() + 100L;
        discount.setName("Updated Discount");
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/discounts/{id}", discountId)
                        .contentType("application/json")
                        .content(asJsonString(discount))
                        .accept("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Discount not found with Id : " + discountId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
    }

    @Test
    public void deleteDiscount() throws Exception {
        discountRepository.save(discount);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/discounts/{id}", discount.getId())
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Resource with ID " + discount.getId() + " deleted successfully."));
    }

    @Test
    public void deleteNonExistingDiscount() throws Exception {
        discountRepository.save(discount);
        Long discountId = discount.getId() + 100L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/discounts/{id}", discountId)
                        .accept("application/json")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Discount not found with Id : " + discountId))
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
}
