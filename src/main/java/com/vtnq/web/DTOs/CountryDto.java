package com.vtnq.web.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.Country}
 */

public class CountryDto  {
    @NotEmpty(message = "Tên Quốc gia không được để trống")
     private String name;


    public @NotEmpty(message = "Tên Quốc gia không được để trống") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Tên Quốc gia không được để trống") String name) {
        this.name = name;
    }
}