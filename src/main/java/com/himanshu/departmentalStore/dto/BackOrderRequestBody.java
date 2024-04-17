package com.himanshu.departmentalStore.dto;

import lombok.Data;

@Data
public class BackOrderRequestBody {
    private Long productId;
    private Long customerId;
    private int quantity;
}

