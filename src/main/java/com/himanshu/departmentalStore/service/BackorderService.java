package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing Backorder entities.
 * Provides methods for retrieving, saving, updating, and deleting backorders.
 */
@Service
public class BackorderService {
    /**
     * Logger for logging messages related to BackorderService class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the BackorderService class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BackorderService.class);


    /**
     * Constant representing the entity type - Backorder.
     */
    private static final String BACKORDERCONSTANT = "Customer";

    /**
     * Autowired field for accessing the BackorderRepository.
     * This repository is used for database operations related to Backorder entities.
     */
    @Autowired
    private BackorderRepository backorderRepository;

    /**
     * Retrieves all backorders.
     * @return A list of all backorders.
     */
    public List<Backorder> getAllBackorders() {
        LOGGER.info("Fetching all backorders");
        return backorderRepository.findAll();
    }

    /**
     * Retrieves a backorder by its ID.
     * @param id The ID of the backorder to retrieve.
     * @return The backorder with the specified ID.
     * @throws ResourceNotFoundException If the backorder with the given ID is not found.
     */
    public Backorder getBackorderById(final Long id) {
        LOGGER.info("Fetching backorder by Id : {}", id);
        return backorderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BACKORDERCONSTANT, "Id", id));
    }

    /**
     * Retrieves all backorders associated with a specific product ID.
     * @param productId The ID of the product to retrieve backorders for.
     * @return A list of all backorders associated with the specified product ID.
     *
     * here no need to through exception for this
     * it is used in order class. to delete backorder and send notification
     */
    public List<Backorder> getAllBackordersByProductId(final Long productId) {
        LOGGER.info("Fetching all backorder having productId: {}", productId);
        return backorderRepository.findByProductId(productId);
    }

    /**
     * Saves a new backorder.
     * @param backorder The backorder to save.
     * @return The saved backorder.
     */
    public Backorder saveBackorder(final Backorder backorder) {
        LOGGER.info("Saving backorder");
        return backorderRepository.save(backorder);
    }

    /**
     * Updates an existing backorder.
     * @param id       The ID of the backorder to update.
     * @param backorder The updated backorder object.
     * @return The updated backorder.
     * @throws ResourceNotFoundException If the backorder with the given ID is not found.
     */
    public Backorder updateBackorder(final Long id, final Backorder backorder) {
        LOGGER.info("Updating backorder");
        boolean isBackorderExist = backorderRepository.existsById(id);
        if (isBackorderExist) {
            backorder.setId(id);
            return backorderRepository.save(backorder);
        } else {
            throw new ResourceNotFoundException(BACKORDERCONSTANT, "Id", id);
        }
    }

    /**
     * Deletes a backorder by its ID.
     * @param id The ID of the backorder to delete.
     * @return True if the backorder was deleted successfully, otherwise false.
     * @throws ResourceNotFoundException If the backorder with the given ID is not found.
     */
    public Boolean deleteBackorder(final Long id) {
        LOGGER.info("Deleting backorder");
        Backorder optionalBackorder = backorderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BACKORDERCONSTANT, "Id", id));
        if (optionalBackorder != null) {
            backorderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
