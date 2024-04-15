package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
