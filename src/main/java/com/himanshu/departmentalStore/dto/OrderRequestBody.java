package com.himanshu.departmentalStore.dto;

import lombok.Data;

@Data
public class OrderRequestBody {
    private Long productId;
    private Long customerId;
    private int quantity;
    private Long discountId;
}

