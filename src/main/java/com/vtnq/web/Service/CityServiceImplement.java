package com.vtnq.web.Service;

import com.vtnq.web.DTOs.City.CityDto;
import com.vtnq.web.Entities.City;
import com.vtnq.web.Entities.Country;
import com.vtnq.web.Repositories.CityRepository;
import com.vtnq.web.Repositories.CountryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImplement implements CityService{
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public boolean addCity(CityDto cityDto) {
        try {
            Country country=countryRepository.findById(cityDto.getCountryId())
                    .orElseThrow(() -> new Exception("Country not found"));
            City city=modelMapper.map(cityDto, City.class);
            city.setCountry(country);
            City insertCity=cityRepository.save(city);
            return insertCity!=null && insertCity.getId()!=null;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<City> findCityAll(int id) {
        try {
            return cityRepository.findByCountryId(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CityDto findCityById(int id) {
        try {
            return modelMapper.map(cityRepository.findById(id), new TypeToken<CityDto>(){}.getType());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existName(String name) {
        return cityRepository.existsByName(name);
    }



    @Override
    public List<City> SearchCityOrCountry(String name) {
        try {
            return cityRepository.SearchCityOrCountry(name);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int CountCity(int id) {
        try {
            return cityRepository.CountCity(id);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
