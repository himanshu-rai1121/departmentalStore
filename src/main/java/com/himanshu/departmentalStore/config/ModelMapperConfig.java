package com.himanshu.departmentalStore.config;

import com.himanshu.departmentalStore.dto.BackOrderRequestBody;
import com.himanshu.departmentalStore.dto.OrderRequestBody;
import com.himanshu.departmentalStore.model.Backorder;
import com.himanshu.departmentalStore.model.Order;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

/**
 * Configuration class for defining ModelMapper bean.
 * This class configures ModelMapper to handle mapping between DTOs (Data Transfer Objects) and domain models.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Defines a bean for ModelMapper.
     * @return ModelMapper instance with strict matching strategy configured.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Set matching strategy to STRICT
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Configure mapping from OrderRequestBody to Order
        modelMapper.createTypeMap(OrderRequestBody.class, Order.class)
            .addMappings(mapping -> {
                mapping.map(OrderRequestBody::getCustomerId, (dest, value) -> dest.getCustomer().setId((Long) value));
                mapping.map(OrderRequestBody::getProductId, (dest, value) -> dest.getProduct().setId((Long) value));
                mapping.map(OrderRequestBody::getDiscountId, (dest, value) -> dest.getDiscount().setId((Long) value));
                mapping.map(OrderRequestBody::getQuantity, Order::setQuantity);
                mapping.map(src -> LocalDateTime.now(), Order::setTimestamp);
            });

        // Configure mapping from BackOrderRequestBody to Backorder
        modelMapper.createTypeMap(BackOrderRequestBody.class, Backorder.class)
                .addMappings(mapping -> {
                    mapping.map(BackOrderRequestBody::getCustomerId, (dest, value) -> dest.getCustomer().setId((Long) value));
                    mapping.map(BackOrderRequestBody::getProductId, (dest, value) -> dest.getProduct().setId((Long) value));
                    mapping.map(BackOrderRequestBody::getQuantity, Backorder::setQuantity);
                    mapping.map(src -> LocalDateTime.now(), Backorder::setTimestamp);
                });
        return modelMapper;
    }
}
