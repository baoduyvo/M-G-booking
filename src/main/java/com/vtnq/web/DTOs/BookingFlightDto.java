package com.vtnq.web.DTOs;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.vtnq.web.Entities.BookingFlight}
 */
public class BookingFlightDto implements Serializable {
    private BigDecimal totalPrice;
    private Boolean status = false;

    public BookingFlightDto() {
    }

    public BookingFlightDto(BigDecimal totalPrice, Boolean status) {
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}