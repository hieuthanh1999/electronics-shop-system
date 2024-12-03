package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Product;
import com.doantotnghiep.nhanambooks.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> getProducts(String keyword, int page, int size, String[] sort) {

        return null;
    }
}
