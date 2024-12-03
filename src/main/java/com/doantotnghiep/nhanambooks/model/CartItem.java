package com.doantotnghiep.nhanambooks.model;

import com.google.common.math.Quantiles;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
public class CartItem {
    private Product product;
    private Integer quantity;
    private Integer finalPrice;

    public CartItem(Product product, Integer quantity) {
        this.setProduct(product);
        this.setQuantity(quantity);
        this.finalPrice =  product.getPrice() - product.getDiscount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return product.equals(cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
