package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.service.BackorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/backorders")
public class BackorderController {

    @Autowired
    private BackorderService backorderService;

    @GetMapping
    public List<Backorder> getAllBackorders() {
        return backorderService.getAllBackorders();
    }

    @GetMapping("/{id}")
    public Backorder getBackorderById(@PathVariable("id") Long id) {
        return backorderService.getBackorderById(id);
    }

    @PostMapping
    public Backorder createBackorder(@RequestBody Backorder backorder) {
        return backorderService.saveBackorder(backorder);
    }

    // No need to update a backorder or delete a backorder in this basic example

    @PutMapping("/{id}")
    public Backorder updateBackorder(@PathVariable("id") Long id, @RequestBody Backorder backorder){
        return backorderService.updateBackorder(id, backorder);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteBackorder(@PathVariable("id") Long id){
        return backorderService.deleteBackorder(id)
                .thenApply(deleted -> {
//                    if (deleted.booleanValue()) {
                    if (deleted) {
                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
                    }
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
    }
}

