package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer){
        customer.setId(id);
        return customerRepository.save(customer);
    }

    public CompletableFuture<Boolean> deleteCustomer(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        });
    }
}
