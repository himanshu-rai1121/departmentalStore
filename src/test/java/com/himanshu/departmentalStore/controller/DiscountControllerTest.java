package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DiscountControllerTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllDiscounts() {
        // Mocking behavior
        List<Discount> discounts = Arrays.asList(
                createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF"),
                createDiscountMock(2L, "20% off", new BigDecimal("20"), LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Spring sale", new BigDecimal("50"), "SPRING20")
        );
        when(discountService.getAllActiveDiscounts()).thenReturn(discounts);

        // Test
        ResponseEntity<List<Discount>> result = discountController.getAllActiveDiscounts();

        // Verification
        assertEquals(2, result.getBody().size());
    }

    @Test
    void getDiscountById() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");
        when(discountService.getDiscountById(discountId)).thenReturn(discount);

        // Test
        ResponseEntity<Discount> result = discountController.getDiscountById(discountId);

        // Verification
        assertEquals(discountId, result.getBody().getId());
    }

    @Test
    void saveDiscount() {
        // Mocking behavior
        Discount discount = createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");
        when(discountService.saveDiscount(discount)).thenReturn(discount);

        // Test
        ResponseEntity<Discount> result = discountController.createDiscount(discount);

        // Verification
        assertEquals(discount.getId(), result.getBody().getId());
    }

    @Test
    void updateDiscount() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", new BigDecimal("0"), "HALFOFF");
        when(discountService.updateDiscount(discountId, discount)).thenReturn(discount);

        discount.setName("40% off");
        // Test
        ResponseEntity<Discount> result = discountController.updateDiscount(discountId, discount);

        // Verification
        assertEquals(discountId, result.getBody().getId());
    }

    @Test
    void deleteDiscount() {
        // Mocking behavior
        Long discountId = 1L;
        when(discountService.deleteDiscount(discountId)).thenReturn(true);

        // Test
        ResponseEntity<String> result = discountController.deleteDiscount(discountId);

        // Verification
        assertEquals(HttpStatus.OK, result.getStatusCode());
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
