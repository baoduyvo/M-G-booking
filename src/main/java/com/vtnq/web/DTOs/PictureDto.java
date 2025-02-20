package com.vtnq.web.DTOs;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Picture}
 */
public class PictureDto implements Serializable {
    private Integer airlineId;
    private Integer amenityId;
    private Integer hotelId;
    private String imageUrl;
    private Boolean isMain = false;
    private Integer roomId;
    private Integer serviceId;
    private Integer userId;

    public PictureDto() {
    }

    public PictureDto(Integer airlineId, Integer amenityId, Integer hotelId, String imageUrl, Boolean isMain, Integer roomId, Integer serviceId, Integer userId) {
        this.airlineId = airlineId;
        this.amenityId = amenityId;
        this.hotelId = hotelId;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.roomId = roomId;
        this.serviceId = serviceId;
        this.userId = userId;
    }

    public Integer getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

    public Integer getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(Integer amenityId) {
        this.amenityId = amenityId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}