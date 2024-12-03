package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.repository.RoleRepository;
import com.doantotnghiep.nhanambooks.repository.UserRepository;
import com.doantotnghiep.nhanambooks.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public void saveUser(Model model, User user) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            model.addAttribute("error", "Email này đã được dùng");
            return;
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setRoles(roleRepository.findByName("USER"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        String htmlMsg = "<meta charset=\"UTF-8\">" + "<h3>Chào mừng bạn đến với Nhã Nam Book </h3>"
                + "<div>Xin chào bạn chúc bạn có một ngày vui vẻ, để kích hoạt tài khoản truy cập:<a href=\"http://localhost:8080/active/"+user.getToken()+"\">Link</a></div>"
                + "<div>Bạn có 10p để xác thực đăng ký</div>";
        emailService.sendHtmlMessage(user.getEmail(),"Kích hoạt tài khoản",htmlMsg);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 60 * 1000);
                    Optional<User> userOptional = userRepository.findByToken(user.getToken());
                    if (userOptional.isPresent()) {
                        userRepository.delete(userOptional.get());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        userRepository.save(user);
    }

    @Override
    public void activeUser(Model model, String token) {
        Optional<User> optionalUser = userRepository.findByToken(token);
        if(optionalUser.isEmpty()){
            model.addAttribute("message","Link xác nhận không hợp lệ hoặc đã hết hạn");
        } else {
            User user = optionalUser.get();
            user.setToken("");
            user.setActive(true);
            userRepository.save(user);
            model.addAttribute("message","Tài khoản của bạn đã được kích hoạt vui lòng đăng nhập");
        }
    }

    @Override
    public void getListUser(Model model, String keyword, int page, int size, String[] sort) {
        try {
            List<User> users = new ArrayList<>();
            String sortField = sort[0];
            String sortDirection = sort[1];

            Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, sortField);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

            Page<User> pageUsers;
            if (keyword == null) {
                pageUsers = userRepository.findAll(pageable);
            } else {
                pageUsers = userRepository.findByNameContainingIgnoreCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            users = pageUsers.getContent();

            model.addAttribute("users", users);
            model.addAttribute("currentPage", pageUsers.getNumber() + 1);
            model.addAttribute("totalItems", pageUsers.getTotalElements());
            model.addAttribute("totalPages", pageUsers.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
    }

    @Override
    public String setUserActive(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            return "redirect:/error?code=not-found";
        }
        User user = optionalUser.get();
        user.setActive(!user.isActive());
        userRepository.save(user);
        return "redirect:/admin/user";
    }

    @Override
    public String createPasswordResetTokenForUser(String email) {
        String token = UUID.randomUUID().toString();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Email không tồn tại";
        }
        User user = optionalUser.get();
        user.setToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public boolean isPasswordResetTokenValid(String token) {
        Optional<User> optionalUser = userRepository.findByToken(token);
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        if (user.getToken().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void resetPassword(String token, String password) {
        Optional<User> optionalUser = userRepository.findByToken(token);
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(password));
        user.setToken("");
        userRepository.save(user);
    }


}
