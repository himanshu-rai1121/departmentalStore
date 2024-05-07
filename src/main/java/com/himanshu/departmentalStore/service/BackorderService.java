package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import com.himanshu.departmentalStore.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final String BACKORDERCONSTANT = "Backorder";

    /**
     * Autowired field for accessing the BackorderRepository.
     * This repository is used for database operations related to Backorder entities.
     */
    @Autowired
    private BackorderRepository backorderRepository;
    /**
     * This repository is used for database operations related to Product entities.
     */
    @Autowired
    private ProductRepository productRepository;
    /**
     * This repository is used for database operations related to Customer entities.
     */
    @Autowired
    private CustomerRepository customerRepository;

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
        if (backorder == null) {
            throw new NullPointerException();
        }
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
        backorderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BACKORDERCONSTANT, "Id", id));
        backorderRepository.deleteById(id);
        return true;
    }
    /**
     * Removes fulfilled backorders associated with the updated product quantity.
     * This method retrieves all backorders associated with the updated product.
     * It then iterates through the list of backorders, checking if each backorder can be fulfilled with the updated product quantity.
     * If a backorder can be fulfilled, it sends a notification or mail and deletes the backorder from the database.
     * @param productId The productId which quantity is updated.
     * @param quantity The updated quantity of the product.
     */
    public void removeFromBackOrder(final Long productId, final int quantity) {
        AtomicInteger productQuantity = new AtomicInteger(quantity);
        LOGGER.info("Fetching all backorder associated with Ordered product");
        List<Backorder> backorderList = getAllBackordersByProductId(productId);
        LOGGER.info("performing operation on backorder.");
        backorderList.forEach(backorder -> {
            if (backorder.getQuantity() <= productQuantity.get()) {
                sendNotification(backorder); // send notification or mail that the product is available now.
                deleteBackorder(backorder.getId()); // Delete backorder
                LOGGER.info("Backorder with Id : {} is fulfilled, Backorder : {}", backorder.getId(), backorder);
                productQuantity.addAndGet(-backorder.getQuantity()); // Reduce product quantity
            }
        });
    }

    /**
     * Check that product and customer exist with requested ID or not.
     * If both product and customer exist then proceed to create or update Backorder.
     * Used in BackorderController to update and create Backorder
     * @param backOrderRequestBody The request body containing backorder details
     */
    public void checkExistence(final BackOrderRequestBody backOrderRequestBody) {
        productRepository
                .findById(backOrderRequestBody.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Id", backOrderRequestBody.getProductId()));
        customerRepository
                .findById(backOrderRequestBody.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "Id", backOrderRequestBody.getCustomerId()));
    }
    /**
     * Sends a notification or mail to inform the customer that the product associated with the backorder is now available.
     * This method is responsible for sending notifications to customers when their backorders can be fulfilled.
     * @param backorder The backorder for which the notification is being sent.
     */
    private void sendNotification(final Backorder backorder) {
        LOGGER.info("Backorder fulfilled, notification send to respective customer");
        /** implement method not yet implemented. */
    }

}
