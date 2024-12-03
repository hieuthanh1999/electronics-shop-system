package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.repository.DiscountRepository;
import com.doantotnghiep.nhanambooks.repository.OrderRepository;
import com.doantotnghiep.nhanambooks.repository.ProductRepository;
import com.doantotnghiep.nhanambooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService{
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final DiscountRepository discountRepository;
    @Override
    public void addBasicInfor(Model model) {
        model.addAttribute("PageTitle","Admin Dashboard");
        model.addAttribute("numUser",userRepository.findAll().size());
        model.addAttribute("numOrder",orderRepository.findAll().size());
        model.addAttribute("sumToDay",orderRepository.countOrderToDay());
        model.addAttribute("stock",productRepository.findProductInStock());
        model.addAttribute("discounts",discountRepository.findCanUse());
        model.addAttribute("pieChart",orderRepository.findDataForPieChart());
        model.addAttribute("curveChart", orderRepository.findDataForLineChart());
    }
}
