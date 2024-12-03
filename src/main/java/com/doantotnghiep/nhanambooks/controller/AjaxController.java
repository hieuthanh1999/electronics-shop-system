package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.dto.AjaxRespone;
import com.doantotnghiep.nhanambooks.dto.CartItemRequest;
import com.doantotnghiep.nhanambooks.dto.DiscountResponse;
import com.doantotnghiep.nhanambooks.dto.OrderRequest;
import com.doantotnghiep.nhanambooks.enums.OrderStatus;
import com.doantotnghiep.nhanambooks.model.*;
import com.doantotnghiep.nhanambooks.repository.*;
import com.doantotnghiep.nhanambooks.service.AjaxService;
import com.doantotnghiep.nhanambooks.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@SessionAttributes("carts")
public class AjaxController {

    private final AjaxService ajaxService;
    private final ProductRepository productRepository;

    @PostMapping (value = "/cart/add")
    @ResponseBody
    public ResponseEntity<?> addProductToCart(@RequestBody CartItemRequest cartItem, @ModelAttribute("carts") Cart cart){

        return ResponseEntity.ok(ajaxService.addProductToCart(cartItem,cart));
    }

    @GetMapping(value = "/cart/delete/{id}")
    void deleteProductInCart(@PathVariable("id") Long productid, HttpServletResponse response, @ModelAttribute("carts") Cart cart) throws IOException {
       ajaxService.deleteProductInCart(productid, response, cart);
    }

    @PostMapping(value = "/discount/exists")
    @ResponseBody
    public ResponseEntity<DiscountResponse> isDiscountExits(@RequestParam String name){
        return ResponseEntity.ok(ajaxService.checkDiscount(name));
    }

    @PostMapping(value = "/order/save")
    public void saveOrder(@ModelAttribute OrderRequest orderRequest, HttpServletResponse response, HttpServletRequest request, @ModelAttribute("carts") Cart cart) throws IOException {
        ajaxService.saveOrder(orderRequest, response,request, cart);
    }

    @ModelAttribute("carts")
    public Cart carts(){
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        return cart;
    }

}
