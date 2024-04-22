package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on Order entities in the database.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
