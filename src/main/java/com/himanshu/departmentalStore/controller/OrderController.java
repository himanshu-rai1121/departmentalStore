package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.service.CustomerService;
import com.himanshu.departmentalStore.service.DiscountService;
import com.himanshu.departmentalStore.service.OrderService;
import com.himanshu.departmentalStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for handling HTTP requests related to orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * The OrderService responsible for handling order-related business logic.
     */
    @Autowired
    private OrderService orderService;

    /**
     * The ProductService responsible for handling product-related business logic.
     */
    @Autowired
    private ProductService productService;

    /**
     * The CustomerService responsible for handling customer-related business logic.
     */
    @Autowired
    private CustomerService customerService;

    /**
     * The DiscountService responsible for handling discount-related business logic.
     */
    @Autowired
    private DiscountService discountService;

    /**
     * Retrieves all orders.
     * @return ResponseEntity containing a list of all orders and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Retrieves an order by its ID.
     * @param id The ID of the order to retrieve
     * @return ResponseEntity containing the order with the specified ID and HTTP status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    /**
     * Creates a new order.
     * @param orderRequestBody The request body containing order details
     * @return ResponseEntity containing the created order and HTTP status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody final OrderRequestBody orderRequestBody) {

        Order order = orderDtoToOrder(orderRequestBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(order));
    }
    /**
     * Converts an OrderRequestBody object to an Order object.
     * @param orderRequestBody The request body containing order details
     * @return The converted Order object
     */
    private Order orderDtoToOrder(final OrderRequestBody orderRequestBody) {
        Customer customer = customerService.getCustomerById(orderRequestBody.getCustomerId());
        Discount discount = discountService.getDiscountById(orderRequestBody.getDiscountId());
        Product product = productService.getProductById(orderRequestBody.getProductId());

        if (customer == null || product == null) {
//            Handle Exception
            return null;
        }
        Order order = new Order();
        order.setDiscount(discount);
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(orderRequestBody.getQuantity());
        return  order;
    }


    /**
     * Updates an existing order.
     * @param id               The ID of the order to update
     * @param orderRequestBody The request body containing updated order details
     * @return ResponseEntity containing the updated order and HTTP status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") final Long id, @RequestBody final OrderRequestBody orderRequestBody) {
        Order order = orderDtoToOrder(orderRequestBody);
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    /**
     * Deletes an order by its ID.
     * @param id The ID of the order to delete
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") final Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Resource with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Resource with ID " + id + " not found.");
        }
    }
}
