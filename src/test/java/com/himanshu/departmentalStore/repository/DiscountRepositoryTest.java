package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.DepartmentalStoreApplication;
import com.himanshu.departmentalStore.model.Discount;
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
class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        discountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        discountRepository.deleteAll();
    }


    @Test
    void testFindAllDiscounts() {
        // Save sample discounts
        Discount discount1 = createDiscountMock(1l, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");
        Discount discount2 = createDiscountMock(2l, "20% off", new BigDecimal("20"), LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Spring sale", new BigDecimal("50"), "SPRING20");
        discountRepository.save(discount1);
        discountRepository.save(discount2);

        // Retrieve all discounts
        List<Discount> discounts = discountRepository.findAll();

        // Check if all discounts are retrieved
        assertEquals(2, discounts.size());

        discountRepository.delete(discount1);
        discountRepository.delete(discount2);
    }

    @Test
    void testFindById() {
        // Save a sample discount
        Discount discount = createDiscountMock(1L, "50% off", new BigDecimal("50.00"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0.00"), "HALFOFF");
        discount.setId(1L);
        Discount savedDiscount = discountRepository.save(discount);

        // Retrieve the discount by ID
        Optional<Discount> foundDiscountOptional = discountRepository.findById(savedDiscount.getId());
        assertTrue(foundDiscountOptional.isPresent());
        Discount foundDiscount = foundDiscountOptional.get();

        // Check if the retrieved discount matches the saved one
        assertEquals(savedDiscount.getId(), foundDiscount.getId());
        assertEquals(savedDiscount.getName(), foundDiscount.getName());
        assertEquals(savedDiscount.getValue(), foundDiscount.getValue());
//        assertEquals(savedDiscount.getStartDate(), foundDiscount.getStartDate());
//        assertEquals(savedDiscount.getEndDate(), foundDiscount.getEndDate());
        assertEquals(savedDiscount.getDescription(), foundDiscount.getDescription());
        assertEquals(savedDiscount.getMinPrice(), foundDiscount.getMinPrice());
        assertEquals(savedDiscount.getCouponCode(), foundDiscount.getCouponCode());

        discountRepository.delete(discount);
    }


    @Test
    void testSaveDiscount() {
        // Create a sample discount
        Discount discount = createDiscountMock(1l,"50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");

        // Save the discount
        Discount savedDiscount = discountRepository.save(discount);

        // Check if the discount is saved with an ID
        assertNotNull(savedDiscount.getId());
        discountRepository.delete(discount);
    }
    @Test
    void testUpdateDiscount() {
        // Save a sample discount
        Discount discount = createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");

        Discount savedDiscount = discountRepository.save(discount);

        // Update the discount's information
        savedDiscount.setName("40% off");
        savedDiscount.setValue(new BigDecimal("40"));
        savedDiscount.setDescription("Updated sale");
        savedDiscount.setCouponCode("UPDATED40");

        // Save the updated discount
        Discount updatedDiscount = discountRepository.save(savedDiscount);

        // Retrieve the updated discount by ID
        Optional<Discount> foundUpdatedDiscountOptional = discountRepository.findById(updatedDiscount.getId());
        assertTrue(foundUpdatedDiscountOptional.isPresent());
        Discount foundUpdatedDiscount = foundUpdatedDiscountOptional.get();

        // Check if the updated discount matches the changes
        assertEquals("40% off", foundUpdatedDiscount.getName());
        assertEquals(new BigDecimal("40.00"), foundUpdatedDiscount.getValue());
        assertEquals("Updated sale", foundUpdatedDiscount.getDescription());
        assertEquals("UPDATED40", foundUpdatedDiscount.getCouponCode());

        // Clean up: delete the discount
        discountRepository.delete(updatedDiscount);
    }

    @Test
    void testDeleteDiscount() {
        // Save a sample discount
        Discount discount = createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");

        Discount savedDiscount = discountRepository.save(discount);

        // Delete the discount
        discountRepository.deleteById(savedDiscount.getId());

        // Check if the discount is deleted
        assertFalse(discountRepository.existsById(savedDiscount.getId()));
    }
    private Discount createDiscountMock(Long id, String name, BigDecimal value, LocalDateTime startDate, LocalDateTime endDate, String description, BigDecimal minPrice, String couponCode) {
        Discount discount = new Discount();
        discount.setId(id);
        discount.setName(name);
        discount.setValue(value);
        discount.setStartDateTime(startDate);
        discount.setEndDateTime(endDate);
        discount.setDescription(description);
        discount.setMinPrice(minPrice);
        discount.setCouponCode(couponCode);
        return discount;
    }
}
