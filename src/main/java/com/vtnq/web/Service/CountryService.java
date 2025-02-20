package com.vtnq.web.Service;

import com.vtnq.web.DTOs.CountryDto;
import com.vtnq.web.Entities.Country;

import java.util.List;

public interface CountryService {
    public boolean addCountry(CountryDto country);
    public List<Country>findAll();
    public Country findCountryById(int id);
    public boolean UpdateCountry(Country country);

    public boolean existCountry(String name);
    public int CountCountry();
}
