package com.cinema.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {

        // Demo dữ liệu (sau này bạn thay bằng Service + Repository)
        model.addAttribute("title", "Cinema Booking System");
        model.addAttribute("welcomeMessage", "Chào mừng bạn đến hệ thống đặt vé xem phim!");

        return "home/index";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("title", "Home");
        return "home/index";
    }
}