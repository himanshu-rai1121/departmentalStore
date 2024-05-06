package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllDiscounts() {
        // Mocking behavior
        List<Discount> discounts = Arrays.asList(
                createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF"),
                createDiscountMock(2L, "20% off", new BigDecimal("20"), LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Spring sale", new BigDecimal("50"), "SPRING20"),
                createDiscountMock(4L, "50% off", new BigDecimal("50"), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), "Half price sale", BigDecimal.ZERO, "HALFOFF")
                );
        when(discountRepository.findAll()).thenReturn(discounts);

        // Test
        List<Discount> result = discountService.getAllActiveDiscounts();

        // Verification
        assertEquals(2, result.size()); // because 1 discount is not active.
    }

    @Test
    void getDiscountById() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF");
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));

        // Test
        Discount result = discountService.getDiscountById(discountId);

        // Verification
        assertNotNull(result);
        assertEquals(discountId, result.getId());
    }

    @Test
    void getDiscountByIdForOrderWithIdNull() {
        // Mocking behavior
        Long discountId = null;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF");
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));

        // Test
        Discount result = discountService.getDiscountByIdForOrder(discountId);

        // Verification
        assertNull(result);
    }

    @Test
    void getDiscountByIdForOrderWithIdNotNull() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF");
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));

        // Test
        Discount result = discountService.getDiscountByIdForOrder(discountId);

        assertNotNull(result);
        assertEquals(discountId, result.getId());
    }

    @Test
    void saveDiscount() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF");
        when(discountRepository.save(discount)).thenReturn(discount);

        // Test
        Discount result = discountService.saveDiscount(discount);

        // Verification
        assertNotNull(result.getId());
    }

    @Test
    void updateDiscount() {
        // Mocking behavior
        Long discountId = 1L;
        Discount discount = createDiscountMock(discountId, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF");
        when(discountRepository.save(discount)).thenReturn(discount);
        when(discountRepository.existsById(discountId)).thenReturn(true);

        discount.setName("40% off");
        // Test
        Discount result = discountService.updateDiscount(discountId, discount);

        // Verification
        assertEquals(discountId, result.getId());
        assertEquals("40% off", result.getName());
    }

    @Test
    void deleteDiscount() {
        // Mocking behavior
        Long discountId = 1L;
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(new Discount()));
        doNothing().when(discountRepository).deleteById(discountId);

        // Test
        boolean result = discountService.deleteDiscount(discountId);

        // Verification
        assertTrue(result);
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
