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


import static org.junit.jupiter.api.Assertions.*;

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
    void testSaveDiscount() {
        // Create a sample discount
        Discount discount = new Discount();
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50"));
        discount.setStartDate(LocalDateTime.now());
        discount.setEndDate(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(BigDecimal.ZERO);
        discount.setCouponCode("HALFOFF");

        // Save the discount
        Discount savedDiscount = discountRepository.save(discount);

        // Check if the discount is saved with an ID
        assertNotNull(savedDiscount.getId());
        discountRepository.delete(discount);
    }

    @Test
    void testFindAllDiscounts() {
        // Save sample discounts
        Discount discount1 = new Discount();
        discount1.setName("50% off");
        discount1.setValue(new BigDecimal("50"));
        discount1.setStartDate(LocalDateTime.now());
        discount1.setEndDate(LocalDateTime.now().plusDays(7));
        discount1.setDescription("Half price sale");
        discount1.setMinPrice(BigDecimal.ZERO);
        discount1.setCouponCode("HALFOFF");
        discountRepository.save(discount1);

        Discount discount2 = new Discount();
        discount2.setName("20% off");
        discount2.setValue(new BigDecimal("20"));
        discount2.setStartDate(LocalDateTime.now());
        discount2.setEndDate(LocalDateTime.now().plusDays(5));
        discount2.setDescription("Spring sale");
        discount2.setMinPrice(new BigDecimal("50"));
        discount2.setCouponCode("SPRING20");
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
        Discount discount = new Discount();
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50.00"));
        discount.setStartDate(LocalDateTime.now());
//        LocalDateTime expectedDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);


        discount.setEndDate(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(new BigDecimal("0.00"));
        discount.setCouponCode("HALFOFF");
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
    void testDeleteDiscount() {
        // Save a sample discount
        Discount discount = new Discount();
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50"));
        discount.setStartDate(LocalDateTime.now());
        discount.setEndDate(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(BigDecimal.ZERO);
        discount.setCouponCode("HALFOFF");
        Discount savedDiscount = discountRepository.save(discount);

        // Delete the discount
        discountRepository.deleteById(savedDiscount.getId());

        // Check if the discount is deleted
        assertFalse(discountRepository.existsById(savedDiscount.getId()));
    }

    @Test
    void testUpdateDiscount() {
        // Save a sample discount
        Discount discount = new Discount();
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50"));
        discount.setStartDate(LocalDateTime.now());
        discount.setEndDate(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(BigDecimal.ZERO);
        discount.setCouponCode("HALFOFF");
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
}
