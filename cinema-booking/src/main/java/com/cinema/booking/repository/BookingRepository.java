package com.cinema.booking.repository;

import com.cinema.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Thống kê theo ngày - nativeQuery để dùng DATE_FORMAT, DATE() của MySQL
    @Query(value =
        "SELECT DATE_FORMAT(paid_at, '%d/%m/%Y') AS label, " +
        "       COUNT(id)                         AS totalBookings, " +
        "       SUM(total_amount)                 AS totalRevenue, " +
        "       SUM(discount_amount)              AS totalDiscount " +
        "FROM bookings " +
        "WHERE payment_status = 'paid' " +
        "  AND paid_at BETWEEN :from AND :to " +
        "GROUP BY DATE(paid_at), DATE_FORMAT(paid_at, '%d/%m/%Y') " +
        "ORDER BY DATE(paid_at)",
        nativeQuery = true)
    List<Object[]> getRevenueByDay(
        @Param("from") LocalDateTime from,
        @Param("to")   LocalDateTime to
    );

    // Thống kê theo tháng - nativeQuery để dùng YEAR(), MONTH() của MySQL
    @Query(value =
        "SELECT DATE_FORMAT(paid_at, '%m/%Y') AS label, " +
        "       COUNT(id)                      AS totalBookings, " +
        "       SUM(total_amount)              AS totalRevenue, " +
        "       SUM(discount_amount)           AS totalDiscount " +
        "FROM bookings " +
        "WHERE payment_status = 'paid' " +
        "  AND YEAR(paid_at) = :year " +
        "GROUP BY YEAR(paid_at), MONTH(paid_at), DATE_FORMAT(paid_at, '%m/%Y') " +
        "ORDER BY MONTH(paid_at)",
        nativeQuery = true)
    List<Object[]> getRevenueByMonth(@Param("year") int year);
}