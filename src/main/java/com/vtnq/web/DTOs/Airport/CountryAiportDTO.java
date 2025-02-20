package com.vtnq.web.DTOs.Airport;

import java.util.List;

public class CountryAiportDTO {
    private String Country;

    public CountryAiportDTO(String country, List<SearchAiportDTO> aiportDTOS) {
        Country = country;
        this.aiportDTOS = aiportDTOS;
    }

    private List<SearchAiportDTO>aiportDTOS;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public List<SearchAiportDTO> getAiportDTOS() {
        return aiportDTOS;
    }

    public void setAiportDTOS(List<SearchAiportDTO> aiportDTOS) {
        this.aiportDTOS = aiportDTOS;
    }
}
