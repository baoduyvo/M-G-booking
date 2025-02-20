package com.vtnq.web.DTOs.Hotel;

public class ImageHotelListDTO {
    private Integer id;
    private String image;
    private int hotelId;

    public ImageHotelListDTO(Integer id, String image, int hotelId) {
        this.id = id;
        this.image = image;
        this.hotelId = hotelId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}
