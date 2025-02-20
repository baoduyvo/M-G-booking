package com.vtnq.web.DTOs.Airline;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Airline}
 */
public class AirlineDto implements Serializable {
    @NotBlank(message = "Name is required")
    private String name;

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
    @Min(value = 1,message = "Country is required")
    private int country_id;
    @NotNull(message = "Image is Required")
    private MultipartFile image;
    public AirlineDto() {
    }

    public AirlineDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}