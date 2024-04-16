package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFountException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.repository.BackorderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BackorderService {
    @Autowired
    private BackorderRepository backorderRepository;

    public List<Backorder> getAllBackorders() {
        return backorderRepository.findAll();
    }

    public Backorder getBackorderById(Long id) {
        return backorderRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFountException("Backorder", "Id", id));
    }

    public List<Backorder> getAllBackordersByProductId(Long orderId) {
        return backorderRepository.findByProductId(orderId);
    }

    public Backorder saveBackorder(Backorder backorder) {
        return backorderRepository.save(backorder);
    }

    public Boolean deleteBackorder(Long id) {
        Optional<Backorder> optionalBackorder = backorderRepository.findById(id);
        if (optionalBackorder.isPresent()) {
            backorderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Backorder updateBackorder(Long id, Backorder backorder){
        backorder.setId(id);
        return backorderRepository.save(backorder);
    }
}
