package com.vtnq.web.DTOs.Airline;

public class ListAirlineDto {
    private int id;

    public ListAirlineDto(int id, String name, String image_url, String nameCountry) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.nameCountry = nameCountry;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    private String name;
    private String image_url;
    private String nameCountry;
}
