package com.himanshu.departmentalStore.controller;

import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

/**
 * Controller class for handling HTTP requests related to orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * Logger for logging messages related to OrderController class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the OrderController class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    /**
     * The OrderService responsible for handling order-related business logic.
     */
    @Autowired
    private OrderService orderService;
    /**
     * The ModelMapper responsible for converting OrderRequestBody (dto) to Order.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Retrieves all orders.
     * @return ResponseEntity containing a list of all orders and HTTP status 200 (OK)
     */
    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders.")
    @ApiResponse(responseCode = "200", description = "Orders found", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class)))
    })
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        LOGGER.info("Received request to fetch all orders.");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Retrieves an order by its ID.
     * @param id The ID of the order to retrieve
     * @return ResponseEntity containing the order with the specified ID and HTTP status 200 (OK)
     */
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
            }),
            @ApiResponse(responseCode = "404", description = "Order not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") final Long id) {
        LOGGER.info("Received request to fetch all orders by Id : {}", id);
        Order order = orderService.getOrderById(id);
        LOGGER.info("Fetched order with id : {}, order : {}", id, order);
        return ResponseEntity.ok(order);
    }
    /**
     * Creates a new order.
     * @param orderRequestBody The request body containing order details
     * @return ResponseEntity containing the created order and HTTP status 201 (Created)
     */
    @Operation(summary = "Create new order", description = "Creates a new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
            }),
            @ApiResponse(responseCode = "400", description = "Customer is null or This Discount can not be applied : amount is less than minimum price"),
            @ApiResponse(responseCode = "404", description = "Product or Customer or Discount or all not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            }),
            @ApiResponse(responseCode = "202", description = "Ordered quantity is more then the quantity left in the stock : Request Accepted : Backorder Created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            })
    })
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody final OrderRequestBody orderRequestBody) {
        LOGGER.info("Received request to create order.");
        LOGGER.info("Converting OrderRequestBody to Order.");
        Order order = orderDtoToOrder(orderRequestBody);
        LOGGER.info("OrderRequestBody converted to Order");
        Order createdOrder = orderService.createOrder(order);
        LOGGER.info("Order Placed.");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    /**
     * Updates an existing order.
     * @param id               The ID of the order to update
     * @param orderRequestBody The request body containing updated order details
     * @return ResponseEntity containing the updated order and HTTP status 200 (OK)
     */
    @Operation(summary = "Update existing order", description = "Updates an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))
            }),
            @ApiResponse(responseCode = "404", description = "Order or Product or Customer or Discount not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Customer or Product or Discount is not same : Only Quantity can be updated : or no change in quantity", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Ordered quantity is more then quantity left in stock", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            }),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") final Long id, @RequestBody final OrderRequestBody orderRequestBody) {
        LOGGER.info("Received request to update the order with Id : {}.", id);
        LOGGER.info("Converting OrderRequestBody to Order.");
        Order order = orderDtoToOrder(orderRequestBody);
        LOGGER.info("OrderRequestBody converted to Order");
        Order updatedOrder = orderService.updateOrder(id, order);
        LOGGER.info("Order placed");
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Deletes an order by its ID.
     * @param id The ID of the order to delete
     * @return ResponseEntity indicating success or failure of the deletion operation
     */
    @Operation(summary = "Delete order by ID", description = "Deletes an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found with given ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = com.himanshu.departmentalStore.exception.ApiResponse.class))
            })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") final Long id) {
        LOGGER.info("Received request to delete order with ID: {}", id);
        orderService.deleteOrder(id);
        LOGGER.info("Order deleted with ID: {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Resource with ID " + id + " deleted successfully.");
    }

    /**
     * Converts an OrderRequestBody object to an Order object.
     * @param orderRequestBody The request body containing order details
     * @return The converted Order object
     */
    private Order orderDtoToOrder(final OrderRequestBody orderRequestBody) {
        LOGGER.info("Checking that Product, customer and discount exist with requested Id or not.");
        orderService.checkExistence(orderRequestBody);
        LOGGER.info("Product, Customer and discount exist with requested Id.");
        return this.modelMapper.map(orderRequestBody, Order.class);    }
}
