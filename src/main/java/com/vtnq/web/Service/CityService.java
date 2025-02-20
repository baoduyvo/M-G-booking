package com.vtnq.web.Service;

import com.vtnq.web.DTOs.City.CityDto;
import com.vtnq.web.Entities.City;

import java.util.List;

public interface CityService {
    public boolean addCity(CityDto cityDto );
    public List<City>findCityAll(int id);
    public CityDto findCityById(int id);
    public boolean existName(String name);

    public List<City>SearchCityOrCountry(String name);
    public int CountCity(int id);
}
