package com.vtnq.web.DTOs;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Booking}
 */
public class BookingDto implements Serializable {
    private Integer paymentStatus;
    private String paypalTransactionId;

    public BookingDto() {
    }

    public BookingDto(Integer paymentStatus, String paypalTransactionId) {
        this.paymentStatus = paymentStatus;
        this.paypalTransactionId = paypalTransactionId;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }

    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
    }
}