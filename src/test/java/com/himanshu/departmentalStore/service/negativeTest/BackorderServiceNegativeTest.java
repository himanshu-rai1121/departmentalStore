package com.himanshu.departmentalStore.service.negativeTest;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import com.himanshu.departmentalStore.service.BackorderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BackorderServiceNegativeTest {

    @Mock
    private BackorderRepository backorderRepository;

    @InjectMocks
    private BackorderService backorderService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getBackorderById_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        when(backorderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> backorderService.getBackorderById(nonExistentId));
    }

    @Test
    void updateBackorder_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        Backorder backorderToUpdate = new Backorder();
        when(backorderRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> backorderService.updateBackorder(nonExistentId, backorderToUpdate));
    }



    @Test
    void saveBackorder_NullObject_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> backorderService.saveBackorder(null));
    }

    @Test
    void updateBackorder_WithNullObject_ShouldThrowResourceNotFoundException() {
        long backorderId = 1L;

        assertThrows(ResourceNotFoundException.class, () -> backorderService.updateBackorder(backorderId, null));
    }

    @Test
    void deleteBackorder_WithNullId_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> backorderService.deleteBackorder(null));
    }

    @Test
    void deleteBackorder_WithNegativeId_ShouldThrowResourceNotFoundException() {// Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> backorderService.deleteBackorder(-1L));
    }
}
