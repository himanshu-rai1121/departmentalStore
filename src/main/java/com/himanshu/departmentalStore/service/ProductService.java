package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for performing operations related to products.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

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
        logger.info("Fetching all products from the database");
        List<Product> products = productRepository.findAll();
        logger.info("Fetched {} products", products.size());
        return products;
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product to retrieve
     * @return The product with the specified ID
     * @throws ResourceNotFoundException if the product with the specified ID does not exist
     */
    public Product getProductById(final Long id) {
        logger.info("Fetching product with ID {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCTCONSTANT, "Id", id));
        logger.info("Fetched product: {}", product);
        return product;    }

    /**
     * Saves a new product or updates an existing one.
     * @param product The product to save or update
     * @return The saved or updated product
     */
    public Product saveProduct(final Product product) {
        logger.info("Saving new product: {}", product);
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
        logger.info("Updating product with ID {}: {}", id, product);
        boolean isProductExist  = productRepository.existsById(id);
        if (isProductExist) {
            product.setId(id);
            return productRepository.save(product);
        } else {
            logger.error("Product with ID {} not found", id);
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
        logger.info("Deleting product with ID {}", id);
        Product optionalProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCTCONSTANT, "Id", id));
        if (optionalProduct!=null) {
            productRepository.deleteById(id);
            return true;
        } else {
            logger.info("Product with ID {} not found", id);
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



