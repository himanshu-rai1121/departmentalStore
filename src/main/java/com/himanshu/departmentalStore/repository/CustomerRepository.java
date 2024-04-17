package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Customer entities in the database.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
