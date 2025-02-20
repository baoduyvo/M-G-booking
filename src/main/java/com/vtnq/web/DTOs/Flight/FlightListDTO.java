package com.vtnq.web.DTOs.Flight;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FlightListDTO { private int id;
    private String nameAirline;
    private String departure_airport;
    private String arrival_airport;
    private String departure_time; // Changed to String to hold formatted date
    private String arrival_time;


    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public FlightListDTO(int id, String nameAirline, String departure_airport, String arrival_airport, LocalDateTime departure_time, LocalDateTime arrival_time) {
        this.id = id;
        this.nameAirline = nameAirline;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.departure_time=departure_time.format(formatter);
        this.arrival_time=arrival_time.format(formatter);
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
    }

    public String getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(String arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public String getNameAirline() {
        return nameAirline;
    }

    public void setNameAirline(String nameAirline) {
        this.nameAirline = nameAirline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
