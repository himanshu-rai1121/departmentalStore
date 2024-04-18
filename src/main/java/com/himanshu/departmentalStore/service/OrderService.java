package com.himanshu.departmentalStore.service;

import com.himanshu.departmentalStore.exception.ResourceNotFoundException;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Discount;
import com.himanshu.departmentalStore.model.Order;
import com.himanshu.departmentalStore.model.Product;
import com.himanshu.departmentalStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private static final String ORDERCONSTANT = "Order";


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
//.........................
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
//.................
    public Order getOrderById(Long order_id){
        return orderRepository
                .findById(order_id)
                .orElseThrow(()->new ResourceNotFoundException("Order", "Id", order_id));
    }
// ... check for quantity and product
    public Order updateOrder(Long order_id, Order order){
        order.setId(order_id);
        return orderRepository.save(order);
    }

    public Boolean deleteOrder(final Long order_id){
        Order optionalOrder = orderRepository
                .findById(order_id)
                .orElseThrow(() -> new ResourceNotFoundException(ORDERCONSTANT, "Id", order_id));
        if (optionalOrder != null) {
            /**
             * increase product quantity
             * then check backorder if it can be fulfilled then send notification and delete that backorder from backorder table.
             * reset discount - optional
             */
            Product product = optionalOrder.getProduct();
            Long productId = product.getId();
            product.setCount(product.getCount()+optionalOrder.getQuantity());
            Product updatedProduct = productService.updateProduct(productId, product);
            /**
             * Atomic Integer is used because the variable inside lambda expression should be final
             * Therefor we can not use productQuantity = productQuantity - b.getQuantity(); in lambda expression
             */
//            get updated product quantity.
            AtomicInteger productQuantity = new AtomicInteger(updatedProduct.getCount());
            List<Backorder> backorderList = backorderService.getAllBackordersByProductId(productId);// get all backorders.
            // Process backorders using lambda expression
            backorderList.forEach(backorder -> {
                if (backorder.getQuantity() <= productQuantity.get()) {
                    sendNotification(backorder); // send notification or mail that the product is available now.
                    backorderService.deleteBackorder(backorder.getId()); // Delete backorder
                    productQuantity.addAndGet(-backorder.getQuantity()); // Reduce product quantity
                }
            });
            orderRepository.deleteById(order_id);
            return true;
        } else {
            return false;
        }

    }
    private void sendNotification(Backorder backorder) {
//        implement method.
    }
}
