package com.vtnq.web.DTOs.Hotel;

public class HotelList {
    private int id;
    private String City;
    private String Country;
    private String ImageUrl;
    private Double Rating;
    private String HotelName;

    public HotelList(int id, String city, String country, String imageUrl, Double rating, String hotelName) {
        this.id = id;
        City = city;
        Country = country;
        ImageUrl = imageUrl;
        Rating = rating;
        HotelName = hotelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Double getRating() {
        return Rating;
    }

    public void setRating(Double rating) {
        Rating = rating;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }
}
