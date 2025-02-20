package com.vtnq.web.Service;

import com.vtnq.web.DTOs.CountryDto;
import com.vtnq.web.Entities.Country;
import com.vtnq.web.Repositories.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImplement implements CountryService{
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public boolean addCountry(CountryDto country) {
        try {
            Country countryDTO=modelMapper.map(country, Country.class);
            countryRepository.save(countryDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Country> findAll() {
        try {
            return countryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Country findCountryById(int id) {
        return countryRepository.findById(id).get();
    }

    @Override
    public boolean UpdateCountry(Country country) {
        try {
            countryRepository.save(country);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean existCountry(String name) {
        try {
        return countryRepository.existsByCountry(name);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int CountCountry() {
        try {
            return countryRepository.CountCountry();
        }catch (Exception exception){
            exception.printStackTrace();
            return 0;
        }
    }
}
