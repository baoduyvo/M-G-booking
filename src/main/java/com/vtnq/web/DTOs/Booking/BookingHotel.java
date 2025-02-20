package com.vtnq.web.DTOs.Booking;

import java.math.BigDecimal;

public class BookingHotel {
    private int id;
    private String name;
    private String city;
    private String typeRoom;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private String imageUrl;
    private BigDecimal price;
    public BookingHotel(int id, String name, String city, String typeRoom, String imageUrl,BigDecimal price) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.typeRoom = typeRoom;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(String typeRoom) {
        this.typeRoom = typeRoom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
