package com.vtnq.web.APIs;

import com.vtnq.web.DTOs.Flight.ResultFlightDTO;
import com.vtnq.web.DTOs.Flight.SearchFlightDTO;
import com.vtnq.web.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Flight")
public class FlightAPI {

    @Autowired
    private FlightService flightService;

    @GetMapping("/All")
    public ResponseEntity<Object> getAllFlight(@RequestBody SearchFlightDTO flight) {
        Map<String,Object> response=new LinkedHashMap<>();
        try {
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping({"detail/{id}"})
    public ResponseEntity<ResultFlightDTO>getFlight(@PathVariable int id) {
        try {
            return new ResponseEntity<ResultFlightDTO>(flightService.FindByIdFlight(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<ResultFlightDTO>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("getFlight")
    public ResponseEntity<List<ResultFlightDTO>> getFlight(
            @RequestParam int departureAirport,
            @RequestParam int arrivalAirport,
            @RequestParam String departureTime,
            @RequestParam String typeFlight,
            @RequestParam int numberPeopleRight) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return new ResponseEntity<List<ResultFlightDTO>>(
                    flightService.SearchFlight(
                            departureAirport,
                            arrivalAirport,
                            LocalDate.parse(departureTime, formatter),
                            typeFlight,
                            numberPeopleRight,
                            LocalDateTime.now()
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
