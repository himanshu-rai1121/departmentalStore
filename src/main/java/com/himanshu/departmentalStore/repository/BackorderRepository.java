package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Backorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackorderRepository extends JpaRepository<Backorder, Long> {
    List<Backorder> findByProductId(Long productId);
}
