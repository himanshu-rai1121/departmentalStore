package com.himanshu.departmentalStore.service.negativeTest;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import com.himanshu.departmentalStore.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class DiscountServiceNegativeTest {

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getDiscountById_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        when(discountRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> discountService.getDiscountById(nonExistentId));
    }

    @Test
    void updateDiscount_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        Discount discountToUpdate = new Discount();
        when(discountRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> discountService.updateDiscount(nonExistentId, discountToUpdate));
    }


    @Test
    void saveDiscount_NullObject_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> discountService.saveDiscount(null));
    }

    @Test
    void updateDiscount_WithNullObject_ShouldThrowResourceNotFoundException() {
        long discountId = 1L;

        assertThrows(ResourceNotFoundException.class, () -> discountService.updateDiscount(discountId, null));
    }

    @Test
    void deleteDiscount_WithNullId_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> discountService.deleteDiscount(null));
    }

    @Test
    void deleteDiscount_WithNegativeId_ShouldThrowResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> discountService.deleteDiscount(-1L));
    }
}
