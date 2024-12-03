package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.dto.OrderRequest;
import com.doantotnghiep.nhanambooks.model.Love;
import com.doantotnghiep.nhanambooks.repository.DiscountRepository;
import com.doantotnghiep.nhanambooks.repository.LoveRepository;
import com.doantotnghiep.nhanambooks.repository.ProductRepository;
import com.doantotnghiep.nhanambooks.repository.UserRepository;
import com.doantotnghiep.nhanambooks.service.CategoryService;
import com.doantotnghiep.nhanambooks.service.UserService;
import com.doantotnghiep.nhanambooks.user.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/")
@SessionAttributes("lovelists")
public class MainController {

    private final UserService userService;
    private final LoveRepository loveRepository;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final UserRepository userRepository;
    @GetMapping()
    public String index(Model model,@ModelAttribute("lovelists") Loves loveList) {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("PageTitle", "Trang chủ");
        model.addAttribute("productCounts", productRepository.countProductByCategory());
        model.addAttribute("newProducts"
                , productRepository.findJustArrivedProduct(PageRequest.of(0,8)));
        model.addAttribute("mostSaleProducts", productRepository.findByMostSale());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().toString().equalsIgnoreCase("anonymousUser")){
            loveList.loves().addAll(loveRepository.findAllByUserId(userRepository.findByEmail(authentication.getName()).get().getId()));
        }

        return "user/index";
    }

    @PostMapping(value = "/love-list/add/{id}")
    @ResponseBody
    public ResponseEntity<List<Love>> addToLoveList(@PathVariable Long id, @ModelAttribute("lovelists") Loves loveList
            , HttpServletResponse response) throws IOException {
        if(loveList.loves().stream().anyMatch(item -> Objects.equals(item.getProduct().getId(), id))){
            return ResponseEntity.ok(loveList.loves());
        }
        Love newLove = new Love();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        newLove.setUser(userRepository.findByEmail(authentication.getName()).get());
        newLove.setProduct(productRepository.findById(id).get());
        loveList.loves().add(loveRepository.save(newLove));
        return ResponseEntity.ok(loveList.loves());
    }

    @GetMapping(value = "/love-list")
    public String loveLists(Model model){
        model.addAttribute("PageTitle","Danh sách yêu thích");
        model.addAttribute("categories", categoryService.getCategories());
        return "user/love-list";
    }

    @GetMapping(value = "/love-list/delete/{id}")
    public String deleteLoveList(@ModelAttribute("lovelists") Loves loveList, @PathVariable Long id){
        loveList.loves().removeIf(item -> item.getId() == id);
        loveRepository.deleteById(id);
        return "redirect:/love-list";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("message","Vui lòng đăng nhaạp vào tài khoản của bạn");
        return "login";
    }

    @GetMapping(value = "/cart")
    public String cart(Model model){
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("PageTitle", "Giỏ hàng");
        return "user/cart";
    }

    @GetMapping(value = "/category/{id}")
    public String productByCategory(Model model,@PathVariable Long id){
        model.addAttribute("PageTitle","Sách");
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("products",productRepository.findByCategoryId(id));
        return "user/product";
    }

    @GetMapping(value = "/cart-content")
    public String cartContent(Model model){

        return "fragments/cart";
    }

    @GetMapping(value = "/checkout")
    public String checkout(Model model, @RequestParam(required = false) String discount){
        model.addAttribute("PageTitle","Thanh toán");
        model.addAttribute("order",new OrderRequest());
        if(!StringUtils.isEmpty(discount)){
            model.addAttribute("discount", discountRepository.findByName(discount));
        }
        return "user/checkout";
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("PageTitle", "Đăng kí");
        model.addAttribute("user",new User());
        return "user/register";
    }

    @PostMapping(value = "/register")
    public String saveUser(Model model, @ModelAttribute User user) throws MessagingException {
        userService.saveUser(model, user);
        return "redirect:/login";
    }

    @GetMapping(value = "/active/{token}")
    public String activeUser(Model model,@PathVariable String token){
        userService.activeUser(model, token);
        return "redirect:/login";
    }
    @ModelAttribute("lovelists")
    public Loves loveList(){
        List<Love> love = new ArrayList<>();
        Loves loves = new Loves(love);
        return loves;
    }

    record Loves(List<Love> loves){

    }


}
