package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Airline.AirlineDto;
import com.vtnq.web.DTOs.Airline.ListAirlineDto;
import com.vtnq.web.DTOs.Airline.UpdateAirlineDTO;
import com.vtnq.web.Entities.Airline;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AirlineService {
    public boolean addAirline(AirlineDto airlineDto);
    public boolean existName(String airlineName);
    public List<ListAirlineDto>findAll();
    public UpdateAirlineDTO findAirlineById(int id);
    public boolean updateArline(UpdateAirlineDTO updateAirlineDTO, MultipartFile file);
    public List<Airline>searchAirline(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight);
    public List<Airline>SearchAirlineArrival(int departureAirport, int arrivalAirport,LocalDate arrivalTime, String TypeFlight);
    public List<Airline>FindByCountryId(int countryId);
    public int CountAirline();
}
