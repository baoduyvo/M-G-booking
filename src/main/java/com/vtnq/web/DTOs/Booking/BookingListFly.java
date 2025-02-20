package com.vtnq.web.DTOs.Booking;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class BookingListFly {
    private int id;
    private String imageUrl;
    private String departureAirport;
    private String arrivalAirport;
    private long durationHours;

    public Long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Long durationDays) {
        this.durationDays = durationDays;
    }

    private long durationDays;

    public String getCityDeparture() {
        return CityDeparture;
    }

    public void setCityDeparture(String cityDeparture) {
        CityDeparture = cityDeparture;
    }

    public String getCityArrival() {
        return CityArrival;
    }

    public void setCityArrival(String cityArrival) {
        CityArrival = cityArrival;
    }

    private long durationMinutes;
    private String durationString;
    private String CityDeparture;
    private String CityArrival;
    public String getNameAirline() {
        return nameAirline;
    }

    public void setNameAirline(String nameAirline) {
        this.nameAirline = nameAirline;
    }

    private String nameAirline;


    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(long durationHours) {
        this.durationHours = durationHours;
    }

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private void calculateAndStoreDuration() {
        if (arrivalTime != null && departureTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            Long days=duration.toDays();
            Long hours=duration.toHours()%24;
            Long minutes=duration.toMinutes()%60;
            this.durationDays=days;
            this.durationHours = hours;
            this.durationMinutes = minutes;
            this.durationString = String.format("%02d:%02d:%02d", days, hours, minutes); // Định dạng HH:mm
        } else {
            this.durationDays = 0;
            this.durationHours = 0;
            this.durationMinutes = 0;
            this.durationString = "00:00:00";
        }
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    private String timeArrival;
    private String timeDepart;

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

    public int getId() {
        return id;
    } private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(time);
    }

    public BookingListFly(int id, String imageUrl, String departureAirport, String arrivalAirport,LocalDateTime departureTime,LocalDateTime arrivalTime,String nameAirline,
                          LocalDateTime TimeArrival,LocalDateTime TimeDepart,String cityDeparture,String cityArrival) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime =arrivalTime;
        calculateAndStoreDuration();
        this.nameAirline = nameAirline;
        this.timeArrival = formatTime(TimeArrival);
        this.timeDepart = formatTime(TimeDepart);
        this.CityDeparture = cityDeparture;
        this.CityArrival = cityArrival;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
}
