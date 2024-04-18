package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
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
        return discountRepository.findAll();
    }

    /**
     * Retrieves a discount by its ID.
     * @param id The ID of the discount to retrieve.
     * @return The discount with the specified ID.
     * @throws ResourceNotFoundException If the discount with the given ID is not found.
     */
    public Discount getDiscountById(final Long id) {
        return discountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNTCONSTANT, "Id", id));
    }
    /**
     * Saves a new discount.
     * @param discount The discount to save.
     * @return The saved discount.
     */
    public Discount saveDiscount(final Discount discount) {
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
        Discount optionalDiscount = discountRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DISCOUNTCONSTANT, "Id", id));
        if (optionalDiscount != null) {
            discountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
