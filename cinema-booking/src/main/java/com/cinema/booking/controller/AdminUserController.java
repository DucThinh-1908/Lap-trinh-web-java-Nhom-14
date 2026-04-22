package com.cinema.booking.controller;

import com.cinema.booking.model.User;
import com.cinema.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller  // ← đổi từ @RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    // Hiển thị danh sách user (HTML)
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user/list";
    }

    // Hiển thị form thêm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/form";
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "admin/user/form";
    }

    // Xử lý thêm/sửa
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            // Mã hoá password ở đây nếu cần
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    // Xoá user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // API trả về JSON (vẫn giữ được nếu cần)
    @GetMapping("/api")
    @ResponseBody  // ← thêm annotation này để trả JSON
    public List<User> apiGetAllUsers() {
        return userRepository.findAll();
    }
}