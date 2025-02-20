package com.vtnq.web.DTOs.Hotel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.vtnq.web.Entities.Hotel}
 */
public class HotelDto implements Serializable {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    @Min(value = 1,message = "city is required")
    private Integer cityId;
    @NotBlank(message = "Description is required")
    private String decription;
    @Min(value = 1,message = "Owner Is required")
    private int ownerId;
    private Integer ratingId;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    private Integer districtId;
    private MultipartFile image;
    private List<MultipartFile>images;

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public HotelDto() {
    }

    public HotelDto(String name, String address, Integer cityId, String decription, Integer ownerId, Integer ratingId) {
        this.name = name;
        this.address = address;
        this.cityId = cityId;
        this.decription = decription;
        this.ownerId = ownerId;
        this.ratingId = ratingId;
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
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    public int getOwnerId() {
        return ownerId;
    }
    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }


    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }
}