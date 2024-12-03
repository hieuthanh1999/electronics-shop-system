package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.model.Contact;
import com.doantotnghiep.nhanambooks.service.CategoryService;
import com.doantotnghiep.nhanambooks.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final CategoryService categoryService;

    @GetMapping(value = "/contact")
    public String contactView(Model model){
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("PageTitle", "Liên hệ");
        model.addAttribute("contact",new Contact());

        return "user/contact";
    }

    @PostMapping(value = "/contact")
    public String saveContact(@ModelAttribute Contact contact){
        contactService.save(contact);
        return "redirect:/";
    }

    @GetMapping(value = "/admin/contact")
    public String listContact(Model model, @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort){
        model.addAttribute("PageTitle","Contact List");
        model.addAttribute("link","admin/contact");
        List<Contact> result = contactService.getListContact(model,keyword,page,size,sort);
        model.addAttribute("contacts", result);
        return "admin/list/contact";
    }
}
