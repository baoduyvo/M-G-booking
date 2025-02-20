package com.vtnq.web.DTOs.Room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class RoomDTO {
    private Integer id;
    @Min(value = 1,message = "Type is required")
    private int type;
    @Min(value = 1,message = "Price is required")
    private BigDecimal price;
    @Min(value = 1,message = "Hotel is required")
    private int idHotel;

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }
    @Min(value = 1,message = "Occupancy is required")
    private Integer occupancy;
    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
