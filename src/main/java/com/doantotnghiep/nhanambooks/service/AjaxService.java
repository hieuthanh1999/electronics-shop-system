package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.dto.CartItemRequest;
import com.doantotnghiep.nhanambooks.dto.DiscountResponse;
import com.doantotnghiep.nhanambooks.dto.OrderRequest;
import com.doantotnghiep.nhanambooks.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AjaxService {
    void saveOrder(OrderRequest orderRequest, HttpServletResponse response, HttpServletRequest request, Cart cart) throws IOException;

    DiscountResponse checkDiscount(String name);

    void deleteProductInCart(Long id, HttpServletResponse response, Cart cart) throws IOException;

    Cart addProductToCart(CartItemRequest cartItem, Cart cart);
}
