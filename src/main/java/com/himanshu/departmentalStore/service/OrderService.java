package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFountException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BackorderService backorderService;


    private Boolean isProductsAvailable(Order order){
        if(order.getProduct().isAvailability() && order.getProduct().getCount()>=order.getQuantity()){
//            reduce product quantity
            Product product = order.getProduct();
            product.setCount(product.getCount()-order.getQuantity());
            productService.updateProduct(product.getId(), product);
            return true;
        } else{
//            Place back order
            Backorder backorder = new Backorder();
            backorder.setQuantity(order.getQuantity());
            backorder.setCustomer(order.getCustomer());
            backorder.setProduct(order.getProduct());
            backorder.setTimestamp(order.getTimestamp());
            backorderService.saveBackorder(backorder);
            return false;
        }

    }

    public Order createOrder(Order order){

        // Apply discount if available

//        Check Product is available or not and it can fillfill the quantity or not
//         if available then place order else place backorder.

        if (isProductsAvailable(order)) {
            // update product---- updated in method isProductsAvailable
            return orderRepository.save(order); // Successfully placed order
        } else {
            // create backorder---- created in method isProductAvailable
            //handle exception and remove below line

            return order;
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Products are not available or cannot fulfill the quantity. Order cannot be placed. Backorder Created");
        }
    }
    private Discount findApplicableDiscount(Order order) {
        // Logic to find applicable discount based on order details
        // For example, fetch active discounts from DiscountService and apply applicable ones
        // Implement your logic to find the most suitable discount for the order
        return null; // Return the applicable discount, or null if no discount is applicable
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long order_id){
        return orderRepository
                .findById(order_id)
                .orElseThrow(()->new ResourceNotFountException("Order", "Id", order_id));
    }

    // no need to update order... only for admin
    public Order updateOrder(Long order_id, Order order){
        order.setId(order_id);
        return orderRepository.save(order);
    }

    public Boolean deleteOrder(Long order_id){
            Optional<Order> optionalOrder = orderRepository.findById(order_id);
            if (optionalOrder.isPresent()) {
                /**
                 * increase product quantity
                 * then check backorder if fulfill then send notification then delete from backorder
                 * reset discount
                 */
                Product product = optionalOrder.get().getProduct();
                product.setCount(product.getCount()+optionalOrder.get().getQuantity());
                Product updatedProduct = productService.updateProduct(product.getId(), product);
                Long productId = updatedProduct.getId();
                /**
                 * int  productQuantity = updatedProduct.getCount();
                 * productQuantity = productQuantity - b.getQuantity();
                 * Atomic Integer is used because the variable inside lambda expression should be final
                 * Therefor we can not use productQuantity = productQuantity - b.getQuantity(); in lambda expression
                 */
                AtomicInteger productQuantity = new AtomicInteger(updatedProduct.getCount());
                List<Backorder> backorderList = backorderService.getAllBackordersByProductId(productId);
                // Process backorders using lambda expression
                backorderList.forEach(backorder -> {
                    if (backorder.getQuantity() <= productQuantity.get()) {
                        // Delete backorder
                        // send notification
                        //add method here to send notification or mail that the product is available now.
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        backorderService.deleteBackorder(backorder.getId());
                        // Reduce product quantity
                        productQuantity.addAndGet(-backorder.getQuantity());
                    }
                });
                orderRepository.deleteById(order_id);
                return true;
            } else {
                return false;
            }
    }
}
