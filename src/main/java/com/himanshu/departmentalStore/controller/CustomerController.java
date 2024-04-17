package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.service.CustomerService;
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
 * Controller class for managing Customer entities.
 * Provides endpoints for handling HTTP requests related to customers.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    /**
     * The CustomerService responsible for handling customer-related business logic.
     */
    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return A list of all customers.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }


    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }


    /**
     * Saves a new customer.
     *
     * @param customer The customer to save.
     * @return The saved customer.
     */
    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody final Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCustomer(customer));
    }


    /**
     * Updates an existing customer.
     *
     * @param id       The ID of the customer to update.
     * @param customer The updated customer object.
     * @return The updated customer.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") final Long id, @RequestBody final Customer customer) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(id, customer));
    }


    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return A CompletableFuture representing the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") final Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
        }
    }

//    @DeleteMapping("/{id}")
//    public CompletableFuture<ResponseEntity<String>> deleteCustomer(@PathVariable("id") Long id) {
//        return customerService.deleteCustomer(id)
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
