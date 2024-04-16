package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/active")
    public List<Discount> getAllActiveDiscounts() {
        return discountService.getAllActiveDiscounts();
    }

    @GetMapping("/{id}")
    public Discount getDiscountById(@PathVariable("id") Long id) {
        return discountService.getDiscountById(id);
    }

    @PostMapping
    public Discount createDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }

    // No need to update a discount or delete a discount in this basic example

    @PutMapping("/{id}")
    public Discount updateDiscount(@PathVariable("id") Long id, @RequestBody Discount discount){
        return discountService.updateDiscount(id, discount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscount(@PathVariable("id") Long id){
        boolean deleted = discountService.deleteDiscount(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
        }
    }

//    @DeleteMapping("/{id}")
//    public CompletableFuture<ResponseEntity<String>> deleteDiscount(@PathVariable("id") Long id){
//        return discountService.deleteDiscount(id)
//                .thenApply(deleted -> {
//                    if (deleted) {
//                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
//                    }
//                })
//                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
//    }
}
