package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.model.Discount;
import com.doantotnghiep.nhanambooks.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountRepository discountRepository;

    @GetMapping(value = "/admin/discount")
    public String discount(Model model){
        model.addAttribute("PageTitle","Discount");
        model.addAttribute("discounts",discountRepository.findAll());
        return "admin/list/discount";
    }

    @GetMapping(value = "/admin/discount/add")
    public String addDiscount(Model model){
        model.addAttribute("PageTitle","Add Discount");
        model.addAttribute("discount", new Discount());
        return "admin/add/discount";
    }

    @PostMapping(value = "/admin/discount/save")
    public String saveDiscount(@ModelAttribute Discount discount){
        if(discount.getId() == null){
            discount.setOncreate(LocalDate.now());
        }
        discountRepository.save(discount);
        return "redirect:/admin/discount";
    }

    @GetMapping(value = "/admin/discount/edit/{id}")
    public String editDiscount(Model model, @PathVariable("id") Long id){
        model.addAttribute("PageTitle","Edit Discount");
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if(optionalDiscount.isEmpty()){
            return "redirect:/error?code=not-found";
        }
        model.addAttribute("discount",optionalDiscount.get());
        return "admin/add/discount";
    }

    @GetMapping(value = "/admin/discount/status/{id}")
    public String updateStatus(@PathVariable("id")Long id){
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if (optionalDiscount.isEmpty()){
            return "redirect:/error?code=not-found";
        }
        Discount discount = optionalDiscount.get();
        discount.setIsActive(!discount.getIsActive());
        discountRepository.save(discount);
        return "redirect:/admin/discount";
    }
}
