package com.vtnq.web.DTOs.Hotel;

public class HotelListDto {
    private int id;
    private String name;
    private String nameDistrict;
    private String imageUrl;
    private  boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public HotelListDto(int id, String name, String nameDistrict, String imageUrl,boolean status) {
        this.id = id;
        this.name = name;
        this.nameDistrict = nameDistrict;
        this.imageUrl = imageUrl;
        this.status = status;
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

    public String getNameDistrict() {
        return nameDistrict;
    }

    public void setNameDistrict(String nameDistrict) {
        this.nameDistrict = nameDistrict;
    }

}
