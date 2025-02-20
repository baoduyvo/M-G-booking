package com.vtnq.web.DTOs.District;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.District}
 */
public class DistrictDto implements Serializable {
    private Integer id;
    @NotBlank(message = "Name is required")
    private String name;

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }
    @Min(value = 1,message = "City is required")
    private int idCity;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DistrictDto() {
    }

    public DistrictDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}