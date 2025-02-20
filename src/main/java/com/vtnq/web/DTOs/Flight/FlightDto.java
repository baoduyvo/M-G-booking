package com.vtnq.web.DTOs.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DTO for {@link com.vtnq.web.Entities.Flight}
 */
public class FlightDto  {
    private Integer id;
    @Min(value = 1,message = "AirLine is required")
    @JsonProperty("airline_id")
    private int airline_id;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @Min(value = 1,message = "Departure Airport is required")
    private int departure_airport;
    @Min(value = 1,message = "Arrival Airport is required")
    private int arrival_airport;
    @NotBlank(message = "Departure Time is required")
    private String departureTime;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id) {
        this.id = id;
    }
    @Min(value = 1,message = "AirLine is required")
    public int getAirline_id() {
        return airline_id;
    }

    public void setAirline_id(@Min(value = 1,message = "AirLine is required") int airline_id) {
        this.airline_id = airline_id;
    }
    public LocalDateTime getDepartureInstant() {
        return convertToInstant(departureTime);
    }

    public LocalDateTime getArrivalInstant() {
        return convertToInstant(arrivalTime);
    }

    @Min(value = 1,message = "Departure Airport is required")
    public int getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(@Min(value = 1,message = "Departure Airport is required")int departure_airport) {
        this.departure_airport = departure_airport;
    }
    @Min(value = 1,message = "Arrival Airport is required")
    public int getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(@Min(value = 1,message = "Arrival Airport is required")int arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public @NotNull(message = "Departure Time is required") String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime( @NotNull(message = "Departure Time is required") String departureTime) {
        this.departureTime = departureTime;
    }

    public @NotNull(message = "Arrival Time is required") String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(@NotNull(message = "Arrival Time is required") String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    private LocalDateTime convertToInstant(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            throw new IllegalArgumentException("Time string cannot be null or empty");
        }

        // Parse string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(timeString, formatter);

        // Convert LocalDateTime to Instant (UTC)
        return localDateTime;
    }
    @NotBlank(message = "Arrival Time is required")
    private String arrivalTime;
    public void setPrice( @NotNull(message = "Price is required") BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getPrice() {
        return price;
    }

}