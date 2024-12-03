package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.model.Category;
import com.doantotnghiep.nhanambooks.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping()
    public String categoryList(Model model){
        model.addAttribute("PageTitle","Category");
        model.addAttribute("categories",categoryService.getCategories());
        return "admin/list/category";
    }

    @GetMapping(value = "/edit/{id}")
    public String editCategory(Model model, @PathVariable Long id){
        model.addAttribute("PageTitle","Edit Category");
        if(categoryService.getCategory(id).isEmpty()){
            model.addAttribute("error","Id không tồn tại");
            return "admin/add/category";
        } else model.addAttribute("category",categoryService.getCategory(id).get());
        return "admin/add/category";
    }

    @PostMapping(value = "/save")
    public String saveCategory(@ModelAttribute Category category){
        categoryService.saveCategory(category);
        return "redirect:/admin/category";
    }

    @GetMapping(value = "/add")
    public String addCategory(Model model){
        model.addAttribute("PageTitle","Add Category");
        model.addAttribute("category", new Category());
        return "/admin/add/category";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteCategory(Model model,@PathVariable Long id){
        boolean deleteSuccess = categoryService.deleteCategory(id);
        if (!deleteSuccess){
            model.addAttribute("error","Không thể xoá Category, vẫn tồn tại sản phẩm thuộc category này");
            return "redirect:/admin/category";
        }
        return "redirect:/admin/category";
    }

}
