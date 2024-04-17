package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Product entities in the database.
 */

public interface ProductRepository extends JpaRepository<Product, Long> {
}
