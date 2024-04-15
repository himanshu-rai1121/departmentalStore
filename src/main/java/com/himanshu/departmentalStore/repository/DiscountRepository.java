package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
