package com.vtnq.web.DTOs.Flight;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ResultFlightDTO {
    private String imageUrl;
    private int id;
    private int idFlight;
    private String nameCity;
    private String nameArrivalAirport;
    public int getIdFlight() {
        return idFlight;
    }

    public String getNameArrivalAirport() {
        return nameArrivalAirport;
    }

    public void setNameArrivalAirport(String nameArrivalAirport) {
        this.nameArrivalAirport = nameArrivalAirport;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    private String timeArrival;
    private String DateDepart;
    private String DateArrival;
    private long durationHours;
    private long durationMinutes;
    private String durationString; // Thêm thuộc tính lưu khoảng cách dưới dạng chuỗi
    private String nameAirport;

    public String getNameAirport() {
        return nameAirport;
    }

    public void setNameAirport(String nameAiport) {
        this.nameAirport = nameAiport;
    }

    public String getDurationString() {
        return durationString;
    }

    private void calculateAndStoreDuration() {
        if (arrivalTime != null && departureTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            this.durationHours = duration.toHours();
            this.durationMinutes = duration.toMinutes() % 60;
            this.durationString = String.format("%02d:%02d", durationHours, durationMinutes); // Định dạng HH:mm
        } else {
            this.durationHours = 0;
            this.durationMinutes = 0;
            this.durationString = "00:00"; // Khoảng cách mặc định
        }
    }

    public String getDateDepart() {
        return DateDepart;
    }

    public void setDateDepart(String dateDepart) {
        DateDepart = dateDepart;
    }

    public String getDateArrival() {
        return DateArrival;
    }

    public void setDateArrival(String dateArrival) {
        DateArrival = dateArrival;
    }

    public String getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(String timeArrival) {
        this.timeArrival = timeArrival;
    }

    public String getTimeDepart() {
        return timeDepart;
    }

    public void setTimeDepart(String timeDepart) {
        this.timeDepart = timeDepart;
    }

    private String timeDepart;

    public String getNameAirline() {
        return nameAirline;
    }

    private String formatDate(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(time);
    }

    public void setNameAirline(String nameAirline) {
        this.nameAirline = nameAirline;
    }

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(time);
    }

    private String nameAirline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private LocalDateTime arrivalTime;
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public ResultFlightDTO() {
    }

    private LocalDateTime departureTime;

    public ResultFlightDTO(int id, String imageUrl, String nameCity, LocalDateTime arrivalTime, LocalDateTime departureTime, BigDecimal price,
                           String nameAirline, LocalDateTime TimeDepart, LocalDateTime TimeArrival, LocalDateTime DateDepart, LocalDateTime DateArrival, String nameAirport,
                           int idFlight, String nameArrivalAirport) {
        this.imageUrl = imageUrl;
        this.nameCity = nameCity;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.price = price;
        this.id = id;
        this.nameAirline = nameAirline;
        this.timeDepart = formatTime(TimeDepart);
        this.timeArrival = formatTime(TimeArrival);
        this.DateDepart = formatDate(departureTime);
        this.DateArrival = formatDate(arrivalTime);
        calculateAndStoreDuration();
        this.nameAirport=nameAirport;
        this.idFlight=idFlight;
        this.nameArrivalAirport=nameArrivalAirport;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
