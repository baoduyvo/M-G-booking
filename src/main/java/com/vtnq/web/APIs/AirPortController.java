package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Airport.AirportDto;
import com.vtnq.web.DTOs.Airport.CountryAiportDTO;
import com.vtnq.web.Service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/AirPort")
public class AirPortController {
    @Autowired
    private AirportService airportService;

    @GetMapping(value = "/SearchAirPort")
    public ResponseEntity<List<CountryAiportDTO>> SearchAirPort(@RequestParam String search) {
        try {
            return new ResponseEntity<List<CountryAiportDTO>>(airportService.SearchAirport(search), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CountryAiportDTO>>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "FindById/{id}")
    public ResponseEntity<AirportDto>FindById(@PathVariable int id) {
        try {
            return new ResponseEntity<AirportDto>(airportService.findById(id), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<AirportDto>(HttpStatus.BAD_REQUEST);
        }
    }
}
