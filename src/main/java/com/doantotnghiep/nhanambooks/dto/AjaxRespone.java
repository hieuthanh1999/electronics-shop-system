package com.doantotnghiep.nhanambooks.dto;

import com.doantotnghiep.nhanambooks.model.Cart;
import com.doantotnghiep.nhanambooks.model.Product;
import lombok.Data;

import java.util.List;
@Data
public class AjaxRespone {
    private String message;
    private Cart cart;
    private List<Product> products;


}
