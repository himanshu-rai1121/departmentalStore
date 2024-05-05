package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing Discount entities.
 * Provides methods for retrieving, saving, updating, and deleting discounts.
 */
@Service
public class DiscountService {

    /**
     * Logger for logging messages related to DiscountService class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the DiscountService class.
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountService.class);

    /**
     * Constant representing the entity type - Discount.
     */
    private static final String DISCOUNTCONSTANT = "Discount";

    /**
     * Autowired field for accessing the DiscountRepository.
     * This repository is used for database operations related to Discount entities.
     */
    @Autowired
    private DiscountRepository discountRepository;


    /**
     * Retrieves all active discounts.
     * Active discounts are those whose start date and end date are within the current date and time.
     * @return A list of all active discounts.
     */
    public List<Discount> getAllActiveDiscounts() {
        LOGGER.info("Fetching all active discounts");
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Discount> discountList = discountRepository.findAll();
        return discountList.stream()
            .filter(discount -> {
                LocalDateTime startDateTime = discount.getStartDateTime();
                LocalDateTime endDateTime = discount.getEndDateTime();
                return currentDateTime.isEqual(startDateTime)  // If current date is equal to start date
                        || currentDateTime.isEqual(endDateTime)  // or equal to end date
                        || (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)); // or between start and end dates
            })
            .toList();
    }
    /**
     * Retrieves all discounts.
     * active/inactive all.
     * @return A list of all discounts.
     */
    public List<Discount> getAllDiscounts() {
        LOGGER.info("Fetching all discounts");
        return discountRepository.findAll();
    }

    /**
     * Retrieves a discount by its ID.
     * @param id The ID of the discount to retrieve.
     * @return The discount with the specified ID.
     * @throws ResourceNotFoundException If the discount with the given ID is not found.
     */
    public Discount getDiscountById(final Long id) {
        LOGGER.info("Fetching discount by ID: {}", id);
        return discountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNTCONSTANT, "Id", id));
    }
    /**
     * Retrieves a discount by its ID.
     * @param id The ID of the discount to retrieve.
     * @return The discount with the specified ID.
     *          or null if not available.
     *          used so that set discount to null if discount not applied
     * in getDiscountById() method it through exception instead of null
     * therefore created a new method which send null
     * it will only be used for order
     */
    public Discount getDiscountByIdForOrder(final Long id) {
        LOGGER.info("fetching discount with ID {}", id);
        if (id == null) {
            return null;
        }
        return discountRepository
                .findById(id)
                .orElse(null);
    }
    /**
     * Saves a new discount.
     * @param discount The discount to save.
     * @return The saved discount.
     */
    public Discount saveDiscount(final Discount discount) {
        if (discount == null) {
            throw new NullPointerException();
        }
        LOGGER.info("Saving discount: {}", discount);
        return discountRepository.save(discount);
    }

    /**
     * Updates an existing discount.
     * @param id       The ID of the discount to update.
     * @param discount The updated discount object.
     * @return The updated discount.
     * @throws ResourceNotFoundException If the discount with the given ID is not found.
     */
    public Discount updateDiscount(final Long id, final Discount discount) {
        LOGGER.info("Updating discount with ID {}: {}", id, discount);
        boolean isDiscountExist = discountRepository.existsById(id);
        if (isDiscountExist) {
            discount.setId(id);
            return discountRepository.save(discount);
        } else {
            throw new ResourceNotFoundException(DISCOUNTCONSTANT, "Id", id);
        }

    }
    /**
     * Deletes a discount by its ID.
     * @param id The ID of the discount to delete.
     * @return True if the discount was deleted successfully, otherwise false.
     * @throws ResourceNotFoundException If the discount with the given ID is not found.
     */
    public Boolean deleteDiscount(final Long id) {
        LOGGER.info("Deleting discount with ID: {}", id);
        discountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNTCONSTANT, "Id", id));
        discountRepository.deleteById(id);
        return true;
    }
}
