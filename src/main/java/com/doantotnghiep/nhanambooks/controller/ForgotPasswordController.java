package com.doantotnghiep.nhanambooks.controller;


import com.doantotnghiep.nhanambooks.service.EmailService;
import com.doantotnghiep.nhanambooks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email) {
        String token = userService.createPasswordResetTokenForUser(email);
        emailService.sendResetPasswordEmail(email, token);
        return "redirect:/login?resetPasswordEmailSent";
    }


    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token) {
        if (userService.isPasswordResetTokenValid(token)) {
            return "reset-password";
        } else {
            return "redirect:/login?invalidToken";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        if (userService.isPasswordResetTokenValid(token)) {
            userService.resetPassword(token, password);
            return "redirect:/";
        }
        return "redirect:/login?invalidToken";
    }
}