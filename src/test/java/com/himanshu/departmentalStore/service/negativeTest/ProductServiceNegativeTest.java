package com.himanshu.departmentalStore.service.negativeTest;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.ProductRepository;
import com.himanshu.departmentalStore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductServiceNegativeTest {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getProductById_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(nonExistentId));
    }

    @Test
    void updateProduct_NonExistentId_ShouldThrowResourceNotFoundException() {
        long nonExistentId = -1;
        Product productToUpdate = new Product();
        when(productRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(nonExistentId, productToUpdate));
    }

    @Test
    void saveProduct_NullObject_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> productService.saveProduct(null));
    }


    @Test
    void updateProduct_WithNullObject_ShouldThrowResourceNotFoundException() {
        long productId = 1L;

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, null));
    }

    @Test
    void deleteProduct_WithNullId_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> productService.deleteProduct(null));
    }

    @Test
    void deleteProduct_WithNegativeId_ShouldThrowResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(-1L));
    }
}