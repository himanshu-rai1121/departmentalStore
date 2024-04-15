package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getAllActiveDiscounts() {
        // Implement logic to fetch active discounts
        // For example, filter discounts based on start and end dates
        // You may also include other conditions based on your business requirements
        // Here, we're assuming the Discount entity has fields startDate and endDate
//        return discountRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());
        return discountRepository.findAll();
    }

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElse(null);
    }

    public Discount updateDiscount(Long id, Discount discount){
        discount.setId(id);
        return discountRepository.save(discount);
    }



    public CompletableFuture<Boolean> deleteDiscount(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Discount> optionalDiscount = discountRepository.findById(id);
            if (optionalDiscount.isPresent()) {
                discountRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        });
    }

    // Additional methods for applying discounts, fetching specific discounts, etc.
}
