package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.config.UploadFirebase;
import com.doantotnghiep.nhanambooks.model.Product;
import com.doantotnghiep.nhanambooks.repository.ProductRepository;
import com.doantotnghiep.nhanambooks.service.CategoryService;
import com.doantotnghiep.nhanambooks.service.ProductService;
import com.doantotnghiep.nhanambooks.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UploadFirebase uploadFirebase;

    @GetMapping(value = "/admin/product")
    public String productList(Model model, @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "price,asc") String[] sort){
        model.addAttribute("PageTitle","Product List");
        model.addAttribute("link","admin/product");
        model.addAttribute("pageLink","/admin/product?");
        getListProduct(model,keyword,page,size,sort);
        return "admin/list/product";
    }

    @GetMapping(value = "/admin/product/add")
    public String addProduct(Model model){
        model.addAttribute(new Product());
        model.addAttribute("categories",categoryService.getCategories());
        model.addAttribute("PageTitle","Add Product");
        return "admin/add/product";
    }

    @GetMapping(value = "/admin/product/edit/{id}")
    public String addProduct(Model model,@PathVariable Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            return "redirect:/error?code=not-found";
        }
        model.addAttribute("product",optionalProduct.get());
        model.addAttribute("categories",categoryService.getCategories());
        model.addAttribute("PageTitle","Edit Product");
        return "admin/add/product";
    }

    @PostMapping(value = "/admin/product/save")
    public String save(Model model, @ModelAttribute("product") Product product, @RequestParam MultipartFile img){
        try {
            String fileName = StringUtils.cleanPath(img.getOriginalFilename());
            String imageUrl = "";
            String uploadDir = "static/images/";
            product.setImage("/images/"+fileName);
            FileUploadUtil.saveFile(uploadDir, fileName, img);
            if(product.getId() == null){
                product.setOncreate(LocalDate.now());
            }
            productRepository.save(product);
        } catch (Exception e){

        }
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/admin/product/delete/{id}")
    public String delete(@PathVariable Long id){
        try {
            productRepository.deleteById(id);
        } catch (Exception e){
            return "redirect:/admin/product";
        }
        return "redirect:/admin/product";
    }

    @GetMapping(value = "/product")
    public String productListUser(Model model, @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "price,asc") String[] sort){
        model.addAttribute("categories",categoryService.getCategories());
        model.addAttribute("PageTitle","Product List");
        getListProduct(model, keyword, page, size, sort);
        return "user/product";
    }

    @GetMapping(value = "/product/{id}")
    public String productDetail(Model model, @PathVariable Long id){
        model.addAttribute("PageTitle","");
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            return "redirect:/error?code=not-found";
        }
        model.addAttribute("product",optionalProduct.get());
        model.addAttribute("productrelative",productRepository.findRelativeProduct(optionalProduct.get().getCategory().getId()));
        return "user/detail";
    }

    private void getListProduct(Model model,String keyword, int page, int size, String[] sort){
        try {
            List<Product> products = new ArrayList<>();
            String sortField = sort[0];
            String sortDirection = sort[1];

            Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, sortField);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

            Page<Product> pageProducts;
            if (keyword == null) {
                pageProducts = productRepository.findAll(pageable);
            } else {
                pageProducts = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            products = pageProducts.getContent();

            model.addAttribute("products", products);
            model.addAttribute("currentPage", pageProducts.getNumber() + 1);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
    }

}
