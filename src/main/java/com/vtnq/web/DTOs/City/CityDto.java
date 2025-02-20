package com.vtnq.web.DTOs.City;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public class CityDto  {
    private Integer id;
    @NotBlank(message = "Name is required")
    private String name;
    @Min(value = 1,message = "Country is required")
    private int CountryId;

    public int getCountryId() {

        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public CityDto() {
    }

    public CityDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}