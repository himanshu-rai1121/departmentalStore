package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Backorder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on Backorder entities in the database.
 */
public interface BackorderRepository extends JpaRepository<Backorder, Long> {
    /**
     * Retrieves a list of backorders by product ID.
     * @param productId The ID of the product to retrieve backorders for
     * @return A list of backorders associated with the specified product ID
     */
    List<Backorder> findByProductId(Long productId);

}
