package com.doantotnghiep.nhanambooks.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productid;

    private int quantity;
}
