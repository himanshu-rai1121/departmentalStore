package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Controller class for handling HTTP requests related to discounts.
 */
@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountController.class);

    /**
     * The DiscountService responsible for handling discount-related business logic.
     */
    @Autowired
    private DiscountService discountService;

    /**
     * Retrieves all active discounts.
     * Based on Start and End DateTime
     * @return List of active discounts
     */
    @GetMapping("/active")
    public ResponseEntity<List<Discount>> getAllActiveDiscounts() {
        logger.info("Received request to Fetch all active discounts");
        List<Discount> discountList = discountService.getAllActiveDiscounts();
        logger.info("Fetched all active discounts : {}", discountList);
        return ResponseEntity.ok(discountList);
    }

    /**
     * Retrieves all discounts.
     * @return List of discounts
     */
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        logger.info("Received request to Fetch all discounts");
        List<Discount> discountList = discountService.getAllDiscounts();
        logger.info("Fetched all discounts : {}", discountList);
        return ResponseEntity.ok(discountList);
    }

    /**
     * Retrieves a discount by its ID.
     * @param id The ID of the discount to retrieve
     * @return The discount with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable("id") final Long id) {
        logger.info("Received request to Fetch discount by Id : {}", id);
        Discount discount = discountService.getDiscountById(id);
        logger.info("Fetched discounts with Id : {}, discount : {}", id, discount);
        return ResponseEntity.ok(discount);
    }

    /**
     * Creates a new discount.
     * @param discount The discount to create
     * @return The created discount
     */
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody final Discount discount) {
        logger.info("Received request to create new discount: {}", discount);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(discountService.saveDiscount(discount));
    }

    // No need to update a discount or delete a discount in this basic example

    /**
     * Updates an existing discount.
     * @param id       The ID of the discount to update
     * @param discount The updated discount information
     * @return The updated discount
     */
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable("id") final Long id, @RequestBody final Discount discount) {
        logger.info("Received request to update discount with ID {}: {}", id, discount);
        return ResponseEntity.ok(discountService.updateDiscount(id, discount));
    }

    /**
     * Deletes a discount by its ID.
     * @param id The ID of the discount to delete
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscount(@PathVariable("id") final Long id) {
        logger.info("Received request to delete discount with ID {}", id);
        boolean deleted = discountService.deleteDiscount(id);
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
//    public CompletableFuture<ResponseEntity<String>> deleteDiscount(@PathVariable("id") Long id){
//        return discountService.deleteDiscount(id)
//                .thenApply(deleted -> {
//                    if (deleted) {
//                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
//                    }
//                })
//                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
//    }
}
