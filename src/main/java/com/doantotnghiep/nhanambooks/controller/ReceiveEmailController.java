package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.service.ReceiveEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReceiveEmailController {
    private final ReceiveEmailService receiveEmailService;

    @PostMapping(value = "/register-email")
    public String saveRegisterEmail(@RequestParam String name, @RequestParam String email){
        receiveEmailService.save(name, email);
        return "redirect:/";
    }

    @GetMapping(value = "/send-email")
    public String sendEmail(@RequestParam String title,@RequestParam String content, @RequestParam(required = false) Long id){
        receiveEmailService.sendEmail(title,content,id);
        return "redirect:/admin";
    }
}
