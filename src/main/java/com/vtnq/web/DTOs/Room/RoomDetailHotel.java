package com.vtnq.web.DTOs.Room;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;

import java.math.BigDecimal;
import java.util.List;

public class RoomDetailHotel {
    public int id;
    private String type;
    private BigDecimal price;
    private String ImageUrl;

    public RoomDetailHotel() {
    }

    public RoomDetailHotel(String type, int id, BigDecimal price, int occupancy) {
        this.type = type;
        this.id = id;
        this.price = price;
        this.occupancy = occupancy;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public RoomDetailHotel(int id, String type, BigDecimal price, int occupancy,String ImageUrl) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.occupancy = occupancy;
        this.ImageUrl = ImageUrl;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public List<AmenitiesList> getAmenitiesLists() {
        return amenitiesLists;
    }

    public void setAmenitiesLists(List<AmenitiesList> amenitiesLists) {
        this.amenitiesLists = amenitiesLists;
    }

    private int occupancy;
    private List<AmenitiesList>amenitiesLists;
}
