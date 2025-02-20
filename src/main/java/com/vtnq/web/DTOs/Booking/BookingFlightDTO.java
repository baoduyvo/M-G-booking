package com.vtnq.web.DTOs.Booking;

import java.math.BigDecimal;
import java.util.List;

public class BookingFlightDTO {

    private BigDecimal TotalPrice;
    private int userId;

    public int getUserId() {
        return userId;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        TotalPrice = totalPrice;
    }


}
