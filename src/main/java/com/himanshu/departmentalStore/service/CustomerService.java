package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFountException;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Customer entities.
 * Provides methods for retrieving, saving, updating, and deleting customers.
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers from the database.
     * @return A list of all customers.
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID, or null if not found.
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(()->new ResourceNotFountException("Customer", "Id", id));
    }

    /**
     * Saves a new customer or updates an existing one.
     *
     * @param customer The customer to save or update.
     * @return The saved or updated customer.
     */
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    /**
     * Updates an existing customer.
     *
     * @param id       The ID of the customer to update.
     * @param customer The updated customer object.
     * @return The updated customer.
     */
    public Customer updateCustomer(Long id, Customer customer) {
        customer.setId(id);
        return customerRepository.save(customer);
    }


    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return A CompletableFuture indicating whether the deletion was successful.
     */
    public Boolean deleteCustomer(Long id) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        }

}
