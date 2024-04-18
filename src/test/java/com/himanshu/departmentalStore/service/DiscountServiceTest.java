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
import static org.mockito.Mockito.*;

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
                createDiscountMock(1L, "50% off", new BigDecimal("50"), LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Half price sale", BigDecimal.ZERO, "HALFOFF"),
                createDiscountMock(2L, "20% off", new BigDecimal("20"), LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Spring sale", new BigDecimal("50"), "SPRING20")
        );
        when(discountRepository.findAll()).thenReturn(discounts);

        // Test
        List<Discount> result = discountService.getAllActiveDiscounts();

        // Verification
        assertEquals(discounts, result);
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
    void saveDiscount() {
        // Mocking behavior
        Discount discount = new Discount();
        discount.setId(1L);
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50"));
        discount.setStartDateTime(LocalDateTime.now());
        discount.setEndDateTime(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(BigDecimal.ZERO);
        discount.setCouponCode("HALFOFF");
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
        Discount discount = new Discount();
        discount.setId(discountId);
        discount.setName("50% off");
        discount.setValue(new BigDecimal("50"));
        discount.setStartDateTime(LocalDateTime.now());
        discount.setEndDateTime(LocalDateTime.now().plusDays(7));
        discount.setDescription("Half price sale");
        discount.setMinPrice(BigDecimal.ZERO);
        discount.setCouponCode("HALFOFF");
        when(discountRepository.save(discount)).thenReturn(discount);
        when(discountRepository.existsById(discountId)).thenReturn(true);

        discount.setName("40% off");
        // Test
        Discount result = discountService.updateDiscount(discountId, discount);

        // Verification
        assertEquals(discountId, result.getId());
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
