package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing Customer entities.
 * This class provides methods for retrieving, saving, updating, and deleting customers.
 */
@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /**
     * Constant representing the entity type - Customer
     */
    private static final String CUSTOMERCONSTANT = "Customer";

    /**
     * Autowired field for accessing the CustomerRepository.
     * This repository is used for database operations related to Customer entities.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves all customers from the database.
     * @return A list of all customers.
     */
    public List<Customer> getAllCustomers() {
        logger.info("Fetching all customers from the database");
        List<Customer> customers = customerRepository.findAll();
        logger.info("Fetched {} customers", customers.size());
        return customers;
    }

    /**
     * Retrieves a customer by their ID.
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID.
     * @throws ResourceNotFoundException If the customer with the given ID is not found.
     */
    public Customer getCustomerById(final Long id) {
        logger.info("Fetching customer with ID {}", id);
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMERCONSTANT, "Id", id));
        logger.info("Fetched customer: {}", customer);
        return customer;
    }

    /**
     * Saves a new customer or updates an existing one.
     * @param customer The customer to save or update.
     * @return The saved or updated customer.
     */
    public Customer saveCustomer(final Customer customer) {
        logger.info("Saving new customer: {}", customer);
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }


    /**
     * Updates an existing customer.
     * @param id       The ID of the customer to update.
     * @param customer The updated customer object.
     * @return The updated customer.
     * @throws ResourceNotFoundException If the customer with the given ID is not found.
     */
    public Customer updateCustomer(final Long id, final Customer customer) {
        logger.info("Updating customer with ID {}: {}", id, customer);
        boolean isCustomerExist = customerRepository.existsById(id);
        if (isCustomerExist) {
            customer.setId(id);
            return customerRepository.save(customer);
        } else {
            logger.error("Customer with ID {} not found", id);
            throw new ResourceNotFoundException(CUSTOMERCONSTANT, "Id", id);
        }
    }


    /**
     * Deletes a customer by their ID.
     * @param id The ID of the customer to delete.
     * @return True if the customer was deleted successfully, otherwise false.
     * @throws ResourceNotFoundException If the customer with the given ID is not found.
     */
    public Boolean deleteCustomer(final Long id) {
        logger.info("Deleting customer with ID {}", id);
        Customer optionalCustomer = customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMERCONSTANT, "Id", id));
        if (optionalCustomer != null) {
            customerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
