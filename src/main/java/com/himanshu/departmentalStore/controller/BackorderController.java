package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.*;
import com.himanshu.departmentalStore.service.BackorderService;
import com.himanshu.departmentalStore.service.CustomerService;
import com.himanshu.departmentalStore.service.DiscountService;
import com.himanshu.departmentalStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/backorders")
public class BackorderController {

    @Autowired
    private BackorderService backorderService;
    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public List<Backorder> getAllBackorders() {
        return backorderService.getAllBackorders();
    }

    @GetMapping("/{id}")
    public Backorder getBackorderById(@PathVariable("id") Long id) {
        return backorderService.getBackorderById(id);
    }

    @PostMapping
    public Backorder createBackorder(@RequestBody BackOrderRequestBody backOrderRequestBody) {
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        return backorderService.saveBackorder(backorder);
    }

    private Backorder backorderDtoToBackorder(BackOrderRequestBody orderRequestBody) {
        Customer customer = customerService.getCustomerById(orderRequestBody.getCustomerId());
        Product product = productService.getProductById(orderRequestBody.getProductId());

        if (customer == null || product==null) {
//            Handle Exception
            return null;
        }
        Backorder order = new Backorder();
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(orderRequestBody.getQuantity());
        return  order;
    }

    // No need to update a backorder or delete a backorder in this basic example

    @PutMapping("/{id}")
    public Backorder updateBackorder(@PathVariable("id") Long id, @RequestBody BackOrderRequestBody backOrderRequestBody){
        Backorder backorder = backorderDtoToBackorder(backOrderRequestBody);
        return backorderService.updateBackorder(id, backorder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBackorder(@PathVariable("id") Long id){
        boolean deleted = backorderService.deleteBackorder(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
        }
    }
//    @DeleteMapping("/{id}")
//    public CompletableFuture<ResponseEntity<String>> deleteBackorder(@PathVariable("id") Long id){
//        return backorderService.deleteBackorder(id)
//                .thenApply(deleted -> {
////                    if (deleted.booleanValue()) {
//                    if (deleted) {
//                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
//                    } else {
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
//                    }
//                })
//                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
//    }
}

