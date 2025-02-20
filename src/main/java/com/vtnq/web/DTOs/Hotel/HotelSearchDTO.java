package com.vtnq.web.DTOs.Hotel;

import java.math.BigDecimal;

public class HotelSearchDTO {
private int id;
private String name;
private String City;
private String Country;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private BigDecimal price;
    public HotelSearchDTO(int id, String name, String city, String country, String image_url,BigDecimal price) {
        this.id = id;
        this.name = name;
        City = city;
        Country = country;
        this.image_url = image_url;
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
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;

}
