package com.vtnq.web.DTOs.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingHotelDTO {
    private LocalDate CheckInDate;
    private LocalDate CheckOutDate;
    private BigDecimal TotalPrice;
    private int userId;

    public LocalDate getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        CheckInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        CheckOutDate = checkOutDate;
    }

    private int typeId;
    private BigDecimal price;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public BigDecimal getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
