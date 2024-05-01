package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.CustomException;
import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.model.Customer;
import com.himanshu.departmentalStore.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing orders in the departmental store system.
 * Provides methods for creating, retrieving, updating, and deleting orders.
 */
@Service
public class OrderService {

    /**
     * Logger for logging messages related to OrderService class.
     * This logger is used to log various messages, such as debug, info, error, etc.,
     * related to the operations performed within the OrderService class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);


    /**
     * Constant representing the entity name for orders.
     */
    private static final String ORDERCONSTANT = "Order";

    /**
     * Repository for accessing order data.
     */
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Service for managing products.
     */
    @Autowired
    private ProductService productService;

    /**
     * Service for managing backorders.
     */
    @Autowired
    private BackorderService backorderService;

    /**
     * Service for managing discount service.
     */
    @Autowired
    private  DiscountService discountService;

    /**
     * Retrieves all orders from the database.
     * @return A list of all orders.
     */
    public List<Order> getAllOrders() {
        LOGGER.info("Fetching all orders");
        List<Order> orders = orderRepository.findAll();
        LOGGER.info("Order Fetched");
        return orders;
    }
    /**
     * Retrieves an order by its ID.
     * @param orderId The ID of the order to retrieve.
     * @return The order with the specified ID.
     * @throws ResourceNotFoundException If the order with the given ID does not exist.
     */
    public Order getOrderById(final Long orderId) {
        LOGGER.info("Fetching all orders with Id : {}", orderId);
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDERCONSTANT, "Id", orderId));
        LOGGER.info("Order fetched with Id : {}, order : {}", orderId, order);
        return  order;
    }
    /**
     * Creates a new order.
     * Applies any available discount and checks if the product is available.
     * If the product is not available, a backorder is created.
     * @param order The order to create.
     * @return The created order.
     * @throws CustomException If the ordered quantity is more than the quantity left in stock, a backorder is created.
     */
    public Order createOrder(final Order order) {
        /** Check Product is available or not and it can fulfill the quantity or not*/
         /** if available then place order else place backorder. */

        LOGGER.info("Placing order.");
        int orderQuantity = order.getQuantity();
        Product product = productService.getProductById(order.getProduct().getId());
        LOGGER.info("Checking isProductAvailable");
        if (isProductsAvailable(product, orderQuantity)) {
            /** Apply discount. */
            LOGGER.info("Calculating total amount");
            BigDecimal totalOrderAmount = findAmount(order, product);
            order.setAmount(totalOrderAmount);
            LOGGER.info("Amount updated in order");
            /** update product -> decrease the available quantity of product. */
            product.setCount(product.getCount() - orderQuantity);
            productService.updateProduct(product.getId(), product);
            LOGGER.info("Updating product quantity");
            LOGGER.info("Saving order");
            return orderRepository.save(order); // Successfully placed order
        } else {
            LOGGER.info("Creating Backorder");
            Backorder backorder = createBackorder(order.getCustomer(), product, orderQuantity, order.getTimestamp());
            LOGGER.info("Backorder saved with Id : {}", backorder.getId());
            throw new CustomException("Ordered quantity is more then quantity left in stock : Backorder created", backorder, HttpStatus.ACCEPTED);
        }
    }
    /**
     * Creates a backorder for the specified customer, product, order quantity, and timestamp.
     * @param customer The customer for whom the backorder is created.
     * @param product The product for which the backorder is created.
     * @param orderQuantity The quantity of the product for backorder.
     * @param timestamp The timestamp indicating when the backorder was created.
     * @return The created backorder.
     */
    private Backorder createBackorder(final Customer customer, final Product product, final int orderQuantity, final LocalDateTime timestamp) {
        Backorder backorder = new Backorder();
        backorder.setQuantity(orderQuantity);
        backorder.setCustomer(customer);
        backorder.setProduct(product);
        backorder.setTimestamp(timestamp);
        LOGGER.info("Saving Backorder");
        return backorderService.saveBackorder(backorder);
    }
    /**
     * Calculates the total amount for the given order, considering any applicable discount.
     * @param order The order for which the total amount is calculated.
     * @return The total amount after applying any applicable discount.
     * @throws CustomException Throws exception if amount is less than minimum price
     */
    private BigDecimal findAmount(final Order order, final Product product) {
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
        if (order.getDiscount() == null) {
            return totalPrice;
        }
        Discount discount = discountService.getDiscountById(order.getDiscount().getId());
        if (discount == null) {
            return totalPrice;
        } else if (discount.getMinPrice() != null
                && discount.getMinPrice().compareTo(totalPrice) <= 0) {
            BigDecimal discountAmount = totalPrice.multiply(discount.getValue().divide(BigDecimal.valueOf(100))); // Calculate discount amount
            totalPrice = totalPrice.subtract(discountAmount); // Subtract discount amount from total price
            return totalPrice;
        }
        LOGGER.error("This discount can not be applied.");
        throw new CustomException("This Discount can not be applied : amount is less than minimum price", discount, HttpStatus.BAD_REQUEST);
         // else discount not applied
    }

    /**
     * Checks if a product is available and has sufficient quantity to fulfill an order.
     * The order can only be placed when both conditions are met:
     * - The product's availability status is true.
     * - The product's quantity is greater than or equal to the order quantity.
     * @param product The product to be checked for availability.
     * @param quantity The quantity of the product required for the order.
     * @return true if the product is available and has sufficient quantity, otherwise return false.
     */
    private Boolean isProductsAvailable(final Product product, final int quantity) {
        if (product.isAvailability() && product.getCount() >= quantity) {
            LOGGER.info("Product available.");
            return true;
        }
        LOGGER.error("Product not available");
        return false;
    }
    /**
     * Updates an existing order by modifying its quantity while ensuring the integrity of related data.
     *
     * <p>
     * This method retrieves the previous order by its ID from the database. It then compares the customer, product,
     * and discount of the previous order with the provided updated order. If any of these components differ, it throws
     * a CustomException indicating that only the quantity can be updated.
     * </p>
     *
     * <p>
     * Next, it calculates the required change in quantity by subtracting the updated quantity from the previous quantity.
     * If the required change is zero, it means there is no change in the quantity, so it throws a CustomException
     * indicating that there is no change in the previous and current quantity.
     * </p>
     *
     * <p>
     * If the required change in quantity is negative, indicating an increase in quantity,
     * means now we have to give some extra quantity to the customer who places the order. it checks whether the product
     * has sufficient stock to fulfill the increase in quantity. If so, it updates the product's count (decrease it by
     * required quantity), updates the product in the database, and saves the updated order.
     *
     * If it can not fulfill the updated order then sends a custom exception that quantity exceeds the available stock.
     * </p>
     *
     * <p>
     * If the required change in quantity is positive, indicating a decrease in quantity,
     * means now we got some extra quantity.
     * saves the updated order, increase the product's count (increases it by the positive required quantity), updates
     * the product in the database,
     * now check the backorder which can be fulfilled by the increase in stock of product (quantity) from any related backorders.
     * </p>
     *
     * @param orderId The ID of the order to update.
     * @param order    The updated order object containing the new quantity.
     * @return The updated order after the modification.
     * @throws ResourceNotFoundException If the order with the given ID does not exist in the database.
     * @throws CustomException           If the provided updated order differs in customer, product, or discount from the
     *                                   previous order, or if there is no change in the quantity, or if the ordered
     *                                   quantity exceeds the available stock.
     */
    public Order updateOrder(final Long orderId, final Order order) {
        LOGGER.info("Updating order with Id : {}", orderId);
        Order previousOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDERCONSTANT, "Id", orderId));
        Product previousProduct = previousOrder.getProduct();
        LOGGER.info("Check if the customer, product, and discount are the same or different");
        if (!previousOrder.getCustomer().getId().equals(order.getCustomer().getId())
            || !previousOrder.getProduct().getId().equals(order.getProduct().getId())
//            || !previousOrder.getDiscount().getId().equals(order.getDiscount().getId())
        ) {
            LOGGER.error("Can't update "
                    + ": Customer or Product or Discount is not same "
                    + ": Only Quantity can be updated");
            throw new CustomException("Can't update  "
                    + ": Customer or Product or Discount is not same "
                    + ": Only Quantity can be updated", null, HttpStatus.BAD_REQUEST);
        }
        int requiredQuantity = previousOrder.getQuantity() - order.getQuantity();
        if (requiredQuantity == 0) {
            LOGGER.error("No change in previous and current quantity : not updated");
            throw new CustomException("No change in previous and current quantity", null, HttpStatus.BAD_REQUEST);
        } else if (requiredQuantity < 0) { //increase in quantity
            LOGGER.info("Product quantity increased : Checking is Product available");
            if (Boolean.TRUE.equals(isProductsAvailable(previousProduct, -requiredQuantity))) {
                // here requiredQuantity is negative, hence decreases overall quantity
                previousProduct.setCount(previousProduct.getCount() + requiredQuantity);
                productService.updateProduct(previousProduct.getId(), previousProduct);
                order.setId(orderId);
                order.setAmount(findAmount(order, previousProduct));
                LOGGER.info("Updated Order, product, orderAmount");
                return orderRepository.save(order);
            } else {
                LOGGER.error("Ordered quantity is more then quantity left in stock : not updated");
                throw new CustomException("Can't update the order :"
                        + " Ordered quantity is more then quantity left in stock", null, HttpStatus.CONFLICT);
            }
        } else { // requiredQuantity > 0  // decrease in quantity
            LOGGER.info("Product quantity decreased");
            order.setId(orderId);
            previousProduct.setCount(previousProduct.getCount() + requiredQuantity); // here requiredQuantity is positive
            productService.updateProduct(previousProduct.getId(), previousProduct); //increase quantity, save
            LOGGER.info("Checking backorder which can be fulfilled");
            backorderService.removeFromBackOrder(previousProduct.getId(), previousProduct.getCount()); //remove from backorder
            order.setAmount(findAmount(order, previousProduct)); // update amount
            LOGGER.info("Updated Order, product, orderAmount");
            return orderRepository.save(order); //update order
        }
    }
    /**
     * Deletes an order by its ID.
     * Increases product quantity and checks if any backorders can be fulfilled.
     * If the order is successfully deleted, it updates the product quantity and removes fulfilled backorders.
     * @param orderId The ID of the order to delete.
     * @return True if the order is deleted successfully, otherwise false.
     * @throws ResourceNotFoundException If the order with the given ID does not exist.
     */
    public Boolean deleteOrder(final Long orderId) {
        LOGGER.info("Deleting order with Id : {}", orderId);
        Order optionalOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDERCONSTANT, "Id", orderId));
        if (optionalOrder != null) {
            /**
             * increase product quantity
             * then check backorder if it can be fulfilled then send notification and delete that backorder from backorder table.
             * reset discount - optional
             */
            LOGGER.info("Updating product");
            Product product = optionalOrder.getProduct();
            Long productId = product.getId();
            product.setCount(product.getCount() + optionalOrder.getQuantity());
            Product updatedProduct = productService.updateProduct(productId, product);
            LOGGER.info("Product quantity increased in product");
            /**
             * Atomic Integer is used because the variable inside lambda expression should be final
             * Therefor we can not use productQuantity = productQuantity - b.getQuantity(); in lambda expression
             */
            LOGGER.info("Checking backorder which can be fulfilled");
            backorderService.removeFromBackOrder(updatedProduct.getId(), updatedProduct.getCount());
            orderRepository.deleteById(orderId);
            LOGGER.info("Order deleted with Id : {}", orderId);
            return true;
        } else {
            LOGGER.info("Order not available with Id : {}", orderId);
            return false;
        }
    }
}
