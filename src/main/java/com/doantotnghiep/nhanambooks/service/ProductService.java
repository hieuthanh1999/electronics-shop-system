package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(String keyword, int page, int size, String[] sort);
}
