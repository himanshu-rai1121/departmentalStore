package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFountException;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getAllActiveDiscounts() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Discount> discountList = discountRepository.findAll();

        return discountList.stream()
            .filter(discount -> {
                LocalDateTime startDateTime = discount.getStartDateTime();
                LocalDateTime endDateTime = discount.getEndDateTime();
                return currentDateTime.isEqual(startDateTime) || // If current date is equal to start date
                        currentDateTime.isEqual(endDateTime) ||   // or equal to end date
                        (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)); // or between start and end dates
            })
            .collect(Collectors.toList());
    }
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElseThrow(()->new ResourceNotFountException("Discount", "Id", id));
    }
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
    public Discount updateDiscount(Long id, Discount discount){
        discount.setId(id);
        return discountRepository.save(discount);
    }



    public Boolean deleteDiscount(Long id) {
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isPresent()) {
            discountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Additional methods for applying discounts, fetching specific discounts, etc.
}
