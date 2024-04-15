package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
