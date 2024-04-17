package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.BackorderService;
import com.himanshu.departmentalStore.service.CustomerService;
import com.himanshu.departmentalStore.service.DiscountService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for handling HTTP requests related to backorders.
 */
@RestController
@RequestMapping("/backorders")
public class BackorderController {

    /**
     * The BackorderService responsible for handling backorder-related business logic.
     */
    @Autowired
    private BackorderService backorderService;
    /**
     * The ProductService responsible for handling product-related business logic.
     */
    @Autowired
    private ProductService productService;

    /**
     * The CustomerService responsible for handling customer-related business logic.
     */
    @Autowired
    private CustomerService customerService;

    /**
     * The DiscountService responsible for handling discount-related business logic.
     */
    @Autowired
    private DiscountService discountService;

    /**
     * Retrieves all backorders.
     * @return ResponseEntity containing a list of all backorders and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Backorder>> getAllBackorders() {
        return ResponseEntity.ok(backorderService.getAllBackorders());
    }

    /**
     * Retrieves a backorder by its ID.
     * @param id The ID of the backorder to retrieve
     * @return ResponseEntity containing the backorder with the specified ID and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Backorder> getBackorderById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(backorderService.getBackorderById(id));
    }

    /**
     * Creates a new backorder.
     * @param backOrderRequestBody The request body containing backorder details
     * @return ResponseEntity containing the created backorder and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Backorder> createBackorder(@RequestBody final BackOrderRequestBody backOrderRequestBody) {
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(backorderService.saveBackorder(backorder));
    }

    /**
     * Converts a BackOrderRequestBody object to a Backorder object.
     * @param orderRequestBody The request body containing backorder details
     * @return The converted Backorder object
     */
    private Backorder backorderDtoToBackorder(final BackOrderRequestBody orderRequestBody) {
        Customer customer = customerService.getCustomerById(orderRequestBody.getCustomerId());
        Product product = productService.getProductById(orderRequestBody.getProductId());

        if (customer == null || product == null) {
//            Handle Exception
            return null;
        }
        Backorder order = new Backorder();
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(orderRequestBody.getQuantity());
        return  order;
    }
    /**
     * Updates an existing backorder.
     * @param id                   The ID of the backorder to update
     * @param backOrderRequestBody The request body containing updated backorder details
     * @return ResponseEntity containing the updated backorder and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Backorder> updateBackorder(@PathVariable("id") final Long id, @RequestBody final BackOrderRequestBody backOrderRequestBody) {
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        return ResponseEntity.ok(backorderService.updateBackorder(id, backorder));
    }
    /**
     * Deletes a backorder by its ID.
     * @param id The ID of the backorder to delete
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBackorder(@PathVariable("id") final Long id) {
        boolean deleted = backorderService.deleteBackorder(id);
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
//    public CompletableFuture<ResponseEntity<String>> deleteBackorder(@PathVariable("id") Long id){
//        return backorderService.deleteBackorder(id)
//                .thenApply(deleted -> {
////                    if (deleted.booleanValue()) {
//                    if (deleted) {
//                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
//                    }
//                })
//                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
//    }
}

