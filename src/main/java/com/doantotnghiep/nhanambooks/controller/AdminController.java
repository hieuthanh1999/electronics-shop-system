package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.service.AdminService;
import com.doantotnghiep.nhanambooks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    @GetMapping()
    public String admin(Model model){
        adminService.addBasicInfor(model);
        return "admin/index";
    }

    @GetMapping(value = "/user")
    public String userManager(Model model, @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(defaultValue = "id,asc") String[] sort){
        model.addAttribute("PageTitle","User List");
        model.addAttribute("link","admin/user");
        model.addAttribute("pageLink","/admin/user?");
        userService.getListUser(model, keyword, page, size, sort);
        return "admin/list/user";
    }

    @GetMapping(value = "/user/status/{id}")
    public String userActive(@PathVariable Long id){
        String link = userService.setUserActive(id);
        return link;
    }
}
