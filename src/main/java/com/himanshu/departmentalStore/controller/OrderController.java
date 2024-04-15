package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dao.OrderRequestBody;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestBody orderRequestBody) {

        Customer customer = customerService.getCustomerById(orderRequestBody.getCustomerId());
        Discount discount = discountService.getDiscountById(orderRequestBody.getDiscountId());


        Product product = productService.getProductById(orderRequestBody.getProductId());

        if (customer == null) {
            // Handle invalid customer ID, e.g., return an error response
            return null;
        }

        Order order = new Order();
        order.setDiscount(discount);
        order.setProduct(product);
        order.setCustomer(customer);
        order.setTimestamp(LocalDateTime.now());
        order.setQuantity(orderRequestBody.getQuantity());

        System.out.println(orderRequestBody.getDiscountId()+" "+ discount.getId()+""+discount.getName());
        System.out.println(order.getDiscount().getCouponCode());



        Object createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    // No need to update an order or delete an order in this basic example

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable("id") Long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteOrder(@PathVariable("id") Long id){
        return orderService.deleteOrder(id)
                .thenApply(deleted -> {
                    if (deleted) {
                        return ResponseEntity.status(HttpStatus.OK).body("Resource with ID " + id + " deleted successfully.");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource with ID " + id + " not found.");
                    }
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the consumer."));
    }
}
