package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.service.BackorderService;
import org.modelmapper.ModelMapper;
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
 * Controller class for handling HTTP requests related to backorders.
 */
@RestController
@RequestMapping("/backorders")
public class BackorderController {
    /**
     * Logger for logging messages related to BackorderController class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the BackorderController class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BackorderController.class);


    /**
     * The BackorderService responsible for handling backorder-related business logic.
     */
    @Autowired
    private BackorderService backorderService;

    /**
     * The ModelMapper responsible for converting BackorderRequestBody (dto) to Backorder.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all backorders.
     * @return ResponseEntity containing a list of all backorders and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Backorder>> getAllBackorders() {
        LOGGER.info("Received request to fetch all backorders");
        List<Backorder> backorders = backorderService.getAllBackorders();
        LOGGER.info("Fetched backorders");
        return ResponseEntity.ok(backorders);
    }

    /**
     * Retrieves a backorder by its ID.
     * @param id The ID of the backorder to retrieve
     * @return ResponseEntity containing the backorder with the specified ID and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Backorder> getBackorderById(@PathVariable("id") final Long id) {
        LOGGER.info("Received request to fetch backorder by ID: {}", id);
        Backorder backorder = backorderService.getBackorderById(id);
        LOGGER.info("Fetched backorder with Id: {}", id);
        return ResponseEntity.ok(backorder);
    }

    /**
     * Creates a new backorder.
     * @param backOrderRequestBody The request body containing backorder details
     * @return ResponseEntity containing the created backorder and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Backorder> createBackorder(@RequestBody final BackOrderRequestBody backOrderRequestBody) {
        LOGGER.info("Received request to create new backorder with request body: {}", backOrderRequestBody);
        LOGGER.info("Converting RequestBody to backorder");
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        LOGGER.info("RequestBody converted to backorder");
        Backorder createdBackorder = backorderService.saveBackorder(backorder);
        LOGGER.info("Backorder created with ID : {}", createdBackorder.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdBackorder);
    }

    /**
     * Updates an existing backorder.
     * @param id                   The ID of the backorder to update
     * @param backOrderRequestBody The request body containing updated backorder details
     * @return ResponseEntity containing the updated backorder and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Backorder> updateBackorder(@PathVariable("id") final Long id, @RequestBody final BackOrderRequestBody backOrderRequestBody) {
        LOGGER.info("Received request to update backorder with ID {}: {}", id, backOrderRequestBody);
        LOGGER.info("Converting RequestBody to backorder");
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        LOGGER.info("RequestBody converted to backorder");
        Backorder updatedBackorder = backorderService.updateBackorder(id, backorder);
        LOGGER.info("Backorder updated with ID : {}", updatedBackorder.getId());
        return ResponseEntity.ok(updatedBackorder);
    }

    /**
     * Deletes a backorder by its ID.
     * @param id The ID of the backorder to delete
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBackorder(@PathVariable("id") final Long id) {
        LOGGER.info("Received request to delete backorder with ID: {}", id);
        backorderService.deleteBackorder(id);
        LOGGER.info("Backorder deleted with ID: {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Resource with ID " + id + " deleted successfully.");
    }

    /**
     * Converts a BackOrderRequestBody object to a Backorder object.
     * @param backorderRequestBody The request body containing backorder details
     * @return The converted Backorder object
     */
    private Backorder backorderDtoToBackorder(final BackOrderRequestBody backorderRequestBody) {
        return this.modelMapper.map(backorderRequestBody, Backorder.class);
    }
}