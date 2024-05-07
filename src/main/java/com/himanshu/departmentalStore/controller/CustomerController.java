package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Controller class for managing Customer entities.
 * Provides endpoints for handling HTTP requests related to customers.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    /**
     * Logger for logging messages related to CustomerController class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the CustomerController class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    /**
     * The CustomerService responsible for handling customer-related business logic.
     */
    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves all customers.
     * @return A list of all customers.
     */
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers.")
    @ApiResponse(responseCode = "200", description = "Customers found", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Customer.class)))
    })
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        LOGGER.info("Received request to retrieve all customers.");
        List<Customer> customers = customerService.getAllCustomers();
        LOGGER.info("Retrieved {} customers.", customers.size());
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }


    /**
     * Retrieves a customer by their ID.
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID.
     */
    @Operation(summary = "Get customer by ID", description = "Retrieves a customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            })
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") final Long id) {
        LOGGER.info("Request received to fetch customer with ID {}", id);
        Customer customer = customerService.getCustomerById(id);
        LOGGER.info("Returning customer with ID {}: {}", id, customer);
        return ResponseEntity.ok(customer);    }


    /**
     * Saves a new customer.
     * @param customer The customer to save.
     * @return The saved customer.
     */
    @Operation(summary = "Save new customer", description = "Saves a new customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
            }),
            @ApiResponse(responseCode = "400", description = "Customer is null")
    })
    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody final Customer customer) {
        LOGGER.info("Request received to save a new customer: {}", customer);
        Customer savedCustomer = customerService.saveCustomer(customer);
        LOGGER.info("Saved customer: {}", savedCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }


    /**
     * Updates an existing customer.
     * @param id       The ID of the customer to update.
     * @param customer The updated customer object.
     * @return The updated customer.
     */
    @Operation(summary = "Update existing customer", description = "Updates an existing customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))
            }),
            @ApiResponse(responseCode = "404", description = "Customer not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") final Long id, @RequestBody final Customer customer) {
        LOGGER.info("Request received to update customer with ID {}: {}", id, customer);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        LOGGER.info("Updated customer with ID {}: {}", id, updatedCustomer);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);    }


    /**
     * Deletes a customer by their ID.
     * @param id The ID of the customer to delete.
     * @return A CompletableFuture representing the result of the deletion.
     */
    @Operation(summary = "Delete customer by ID", description = "Deletes a customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            }
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") final Long id) {
        LOGGER.info("Request received to delete customer with ID {}", id);
        customerService.deleteCustomer(id);
        LOGGER.info("Customer with ID {} deleted successfully", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Resource with ID " + id + " deleted successfully.");
    }
}
