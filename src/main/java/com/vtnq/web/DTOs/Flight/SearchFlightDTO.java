package com.vtnq.web.DTOs.Flight;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SearchFlightDTO {
    private int departureAirport;
    private int arrivalAirport;
    private String departureTime;
    private List<String>validationErrors=new ArrayList<>();
    public String getValidationErrors(){
        StringBuilder errorMessages = new StringBuilder("Validation errors:\n");

        // Assuming validationErrors is a list of error messages
        validationErrors.forEach(error ->
                errorMessages.append(String.format(" %s\n", error))
        );

        return errorMessages.toString();
    }
    public boolean hasErrors(){
        return !validationErrors.isEmpty();
    }
    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
        if(selectedHotel && checkInTime.isEmpty()) {
           validationErrors.add("Check In Required");
        }
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
        if (selectedHotel && (checkOutTime == null || checkOutTime.isEmpty())) {
            validationErrors.add("Check Out Required");
        }
    }
    public static Long calculateDaysBetween(String checkInTime, String checkOutTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate= LocalDate.parse(checkInTime, formatter);
        LocalDate checkOutDate= LocalDate.parse(checkOutTime, formatter);
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    @JsonProperty("TypeFlight")
    private String TypeFlight;
    private boolean selectedHotel;
    private String checkInTime;
    private String checkOutTime;
    public boolean isRoundTrip() {

        return IsRoundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        IsRoundTrip = roundTrip;
        if(IsRoundTrip && (this.arrivalTime==null || this.arrivalTime.isEmpty())){
            validationErrors.add("Arrival time Required");
        }
    }

    private boolean IsRoundTrip;

    public int getNumberPeopleRight() {
        return numberPeopleRight;
    }

    public void setNumberPeopleRight(int numberPeopleRight) {
        this.numberPeopleRight = numberPeopleRight;
    }

    private int numberPeopleRight;
    public int getQuantityRoom() {
        return QuantityRoom;
    }

    public void setQuantityRoom(int quantityRoom) {
        QuantityRoom = quantityRoom;
    }

    private int idCity;
    private int QuantityRoom;

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
        if(selectedHotel && idCity==0){
            validationErrors.add("city Required");
        }

    }

    public String getArrivalTime() {
        return arrivalTime;

    }

    public boolean isSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(boolean selectedHotel) {
        this.selectedHotel = selectedHotel;
        if (selectedHotel && (checkOutTime == null || checkOutTime.isEmpty())) {
            validationErrors.add("Check Out Required");
        }
        if(selectedHotel && (checkInTime == null || checkInTime.isEmpty())){
            validationErrors.add("Check In Required");
        }
        if(selectedHotel && idCity==0){
            validationErrors.add("City Required");
        }
    }

    public void setArrivalTime(String arrivalTime) {
        if(IsRoundTrip &&(arrivalTime==null || arrivalTime.isEmpty())){
            validationErrors.add("Arrival time Required");
        }
        this.arrivalTime = arrivalTime;
    }

    private String arrivalTime;
    public String getTypeFlight() {
        return TypeFlight;
    }

    public void setTypeFlight(String typeFlight) {
        TypeFlight = typeFlight;
    }

    public int getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(int arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
        if(arrivalAirport==0){
            validationErrors.add("Arrival Airport Required");
        }
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        if(departureTime.isEmpty() || departureTime==null){
            validationErrors.add("Departure Time Required");
        }
    }

    public int getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(int departureAirport) {
        this.departureAirport = departureAirport;
        if(departureAirport==0){
            validationErrors.add("Departure Airport Required");
        }
    }
}
