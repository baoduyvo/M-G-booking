package com.vtnq.web.DTOs;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.vtnq.web.Entities.DetailFlight}
 */
public class DetailFlightDto implements Serializable {
    private BigDecimal price;
    private Integer quantity;
    private String type;

    public DetailFlightDto() {
    }

    public DetailFlightDto(BigDecimal price, Integer quantity, String type) {
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}