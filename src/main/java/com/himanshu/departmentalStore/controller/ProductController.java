package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

/**
 * Controller class for handling HTTP requests related to products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    /**
     * The ProductService responsible for handling product-related business logic.
     */
    @Autowired
    private ProductService productService;

    /**
     * Retrieves all products.
     *
     * @return ResponseEntity with a list of products and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return ResponseEntity with the product and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Saves a new product.
     *
     * @param product The product to be saved
     * @return ResponseEntity with the saved product and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody final Product product) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.saveProduct(product));
    }

    /**
     * Updates an existing product.
     *
     * @param id      The ID of the product to update
     * @param product The updated product information
     * @return ResponseEntity with the updated product and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") final Long id, @RequestBody final Product product) {
        return ResponseEntity
                .ok(productService.updateProduct(id, product));
    }
    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return ResponseEntity with a success or error message and appropriate HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") final Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Resource with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Resource with ID " + id + " not found.");
        }
    }


//    @DeleteMapping("/{id}")
//    public CompletableFuture<ResponseEntity<String>> deleteProduct(@PathVariable("id") Long id){
//        return CompletableFuture.supplyAsync(() -> {
//            boolean deleted = productService.deleteProduct(id);
//                    if (deleted) {
//                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
//                    }
//                })
//                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
//
//    }
}
