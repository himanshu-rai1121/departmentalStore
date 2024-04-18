package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for performing operations related to products.
 */
@Service
public class ProductService {
    /**
     * A static string representing the entity name "Product" used for exception handling.
     * Used as a constant to reduce redundancy.
     */
    private static final String PRODUCTCONSTANT = "Product";

    /**
     * Repository for managing products in the database.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from the database.
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product to retrieve
     * @return The product with the specified ID
     * @throws ResourceNotFoundException if the product with the specified ID does not exist
     */
    public Product getProductById(final Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCTCONSTANT, "Id", id));
    }

    /**
     * Saves a new product or updates an existing one.
     * @param product The product to save or update
     * @return The saved or updated product
     */
    public Product saveProduct(final Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product with the specified ID.
     * @param id      The ID of the product to update
     * @param product The updated product information
     * @return The updated product
     * @throws ResourceNotFoundException if the product with the specified ID does not exist
     */
    public Product updateProduct(final Long id, final Product product) {
        boolean isProductExist  = productRepository.existsById(id);
        if (isProductExist) {
            product.setId(id);
            return productRepository.save(product);
        } else {
            throw new ResourceNotFoundException(PRODUCTCONSTANT, "Id", id);
        }
    }

    /**
     * Deletes a product with the specified ID.
     * @param id The ID of the product to delete
     * @return true if the product was deleted successfully, false otherwise
     * @throws ResourceNotFoundException ResourceNotFountException if the product with the specified ID does not exist
     */
    public boolean deleteProduct(final Long id) {
        Product optionalProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCTCONSTANT, "Id", id));
        if (optionalProduct!=null) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

//    public CompletableFuture<Boolean> deleteProduct(Long id) {
//        return CompletableFuture.supplyAsync(() -> {
//            Optional<Product> optionalProduct = productRepository.findById(id);
//            if (optionalProduct.isPresent()) {
//                productRepository.deleteById(id);
//                return true;
//            } else {
//                return false;
//            }
//        });
//    }
}



