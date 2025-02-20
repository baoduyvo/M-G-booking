package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Airport.AirportDto;
import com.vtnq.web.DTOs.Airport.CountryAiportDTO;
import com.vtnq.web.Entities.Airport;

import java.util.List;

public interface AirportService {
    public boolean save(AirportDto airportDto);
    public List<Airport>findAll(int id);
    public boolean existByName(String name);
    public AirportDto findById(int id);
    List<Airport>findAll();
    public List<CountryAiportDTO>SearchAirport(String SearchName);
    public int CountAirport(int id);
    public boolean existAirportInCity(int id);
}
