package com.vtnq.web.DTOs.Hotel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.jsoup.Jsoup;

public class HotelUpdateDTO {
    private int id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    private String addressText;

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Min(value = 1,message = "City is required")
    private Integer cityId;
    @NotBlank(message = "Description is required")
    private String decription;
    private String imageUrl;
    public String cleanHtml(String input){
        if(input!=null){
            return Jsoup.parse(input).text();
        }
        return "";
    }
    public HotelUpdateDTO(int id, String name, String address, Integer cityId, String decription, Integer ownerId, int district_id, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cityId = cityId;
        this.decription = decription;
        this.ownerId = ownerId;
        this.district_id = district_id;
        this.imageUrl = imageUrl;
        this.addressText=cleanHtml(address);
    }

    private Integer ownerId;



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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    private int district_id;
}
