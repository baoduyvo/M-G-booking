package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.City.CityDto;
import com.vtnq.web.Entities.City;
import com.vtnq.web.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/city")
public class CityController {
    @Autowired
    private CityService cityService;
    @GetMapping(value = "SearchHotelByCityOrHotel")
    public ResponseEntity<List<City>> SearchHotel(@RequestParam("name")String name) {
        try {
            return new ResponseEntity<List<City>>(cityService.SearchCityOrCountry(name), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<City>>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "FindById/{id}")
    public ResponseEntity<CityDto>FindById(@PathVariable int id) {
        try {
            return new ResponseEntity<CityDto>(cityService.findCityById(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<CityDto>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "FindCityByCountry/{id}")
    public ResponseEntity<List<City>>FindCityByCountry(@PathVariable("id")int id){
        try {
            return new ResponseEntity<List<City>>(cityService.findCityAll(id),HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<City>>(HttpStatus.BAD_REQUEST);
        }
    }
}
