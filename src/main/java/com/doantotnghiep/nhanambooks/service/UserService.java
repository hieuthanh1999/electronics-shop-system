package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.user.User;
import jakarta.mail.MessagingException;
import org.springframework.ui.Model;

public interface UserService {
    void saveUser(Model model, User user) throws MessagingException;
    void activeUser(Model model, String token);
    void getListUser(Model model, String keyword, int page, int size, String[] sort);
    String setUserActive(Long id);

    public String createPasswordResetTokenForUser(String email);
    public boolean isPasswordResetTokenValid(String token);
    public void resetPassword(String token, String password);
}
