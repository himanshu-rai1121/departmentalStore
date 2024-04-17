package com.himanshu.departmentalStore.repository;

import com.himanshu.departmentalStore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
