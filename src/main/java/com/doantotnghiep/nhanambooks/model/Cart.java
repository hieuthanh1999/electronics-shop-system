package com.doantotnghiep.nhanambooks.model;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;

    private Integer total;

    public void addCartItem(Product product,Integer quantity){
        for (CartItem item: items) {
            if(item.getProduct().getId().equals(product.getId())){
                item.setQuantity(quantity == 0 ? (item.getQuantity() + 1) : quantity);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public void updateCartItem(CartItem cartItem){
        int index =items.indexOf(cartItem);
        if(index != -1){
            items.get(index).setQuantity(cartItem.getQuantity());
        }
    }
}
