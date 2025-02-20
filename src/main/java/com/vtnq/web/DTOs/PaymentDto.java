package com.vtnq.web.DTOs;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Payment}
 */
public class PaymentDto implements Serializable {
    private String name;

    public PaymentDto() {
    }

    public PaymentDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}