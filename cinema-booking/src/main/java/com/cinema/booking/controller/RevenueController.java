package com.cinema.booking.controller;

import com.cinema.booking.repository.BookingRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/revenue")
public class RevenueController {

    private final BookingRepository bookingRepository;

    public RevenueController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public String index(
            @RequestParam(defaultValue = "day") String type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @RequestParam(required = false) Integer year,
            Model model
    ) {
        // Giá trị mặc định
        LocalDate today = LocalDate.now();
        if (from == null) from = today.withDayOfMonth(1);
        if (to   == null) to   = today;
        if (year == null) year = today.getYear();

        // Lấy dữ liệu từ repository → Object[]: [label, totalBookings, totalRevenue, totalDiscount]
        List<Object[]> revenueList;
        if ("month".equals(type)) {
            revenueList = bookingRepository.getRevenueByMonth(year);
        } else {
            LocalDateTime fromDT = from.atStartOfDay();
            LocalDateTime toDT   = to.atTime(23, 59, 59);
            revenueList = bookingRepository.getRevenueByDay(fromDT, toDT);
        }

        // Tính tổng doanh thu cho summary card
        BigDecimal totalRevenue = revenueList.stream()
                .map(row -> (BigDecimal) row[2])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalBookings = revenueList.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();

        // Chuỗi JSON cho Chart.js
        String chartLabels = revenueList.stream()
                .map(row -> "\"" + row[0] + "\"")
                .collect(Collectors.joining(","));

        String chartValues = revenueList.stream()
                .map(row -> ((BigDecimal) row[2]).toPlainString())
                .collect(Collectors.joining(","));

        model.addAttribute("type",          type);
        model.addAttribute("from",          from);
        model.addAttribute("to",            to);
        model.addAttribute("year",          year);
        model.addAttribute("revenueList",   revenueList);
        model.addAttribute("totalRevenue",  totalRevenue);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("chartLabels",   chartLabels);
        model.addAttribute("chartValues",   chartValues);

        return "admin/revenue/index";
    }
}