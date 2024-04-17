package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Discount entities in the database.
 */
public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
