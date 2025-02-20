package com.vtnq.web.DTOs.Airline;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateAirlineDTO {
    private int id;

    public UpdateAirlineDTO(int id, String name, int idCountry, String image) {
        this.id = id;
        this.name = name;
        this.idCountry = idCountry;
        this.image = image;
    }
    @NotBlank(message = "Name is required")
    private  String name;
    @Min(value = 1,message = "Country is required")
    private int idCountry;

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

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

}
