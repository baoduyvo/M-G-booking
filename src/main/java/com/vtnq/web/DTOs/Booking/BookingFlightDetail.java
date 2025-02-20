package com.vtnq.web.DTOs.Booking;

import java.math.BigDecimal;

public class BookingFlightDetail {
    private int id;
    private int flightId;



    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    private BigDecimal totalPrice;
}
