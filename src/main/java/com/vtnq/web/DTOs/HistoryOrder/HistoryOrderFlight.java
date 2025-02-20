package com.vtnq.web.DTOs.HistoryOrder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryOrderFlight {
    private int id;
    private String flightNo;
    private String ArrivalTime;
    private String DepartAirport;
    private String ArrivalAirport;

    public String getDepartAirport() {
        return DepartAirport;
    }

    public void setDepartAirport(String departAirport) {
        DepartAirport = departAirport;
    }

    public String getArrivalAirport() {
        return ArrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        ArrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    private String DepartureTime;

    private String formatInstant(LocalDateTime instant) {
        // Convert Instant to ZonedDateTime with your desired time zone
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault()); // Use system default or specify another zone

        // Define the format: "dd MM yyyy HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm");

        // Format the ZonedDateTime and return the string
        return zonedDateTime.format(formatter);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    private String index;
    public HistoryOrderFlight(int id,String index,String flightNo,LocalDateTime arrivalTime,LocalDateTime departureTime,String DepartAirport,String ArrivalAirport) {

        this.id = id;
        this.index = index;
        this.flightNo = flightNo;
        this.DepartureTime=formatInstant(departureTime);
        this.ArrivalTime=formatInstant(arrivalTime);
        this.DepartAirport=DepartAirport;
        this.ArrivalAirport=ArrivalAirport;

    }
    public static List<HistoryOrderFlight>mapHistoryOrderFlight(List<Object[]>results) {
        List<HistoryOrderFlight> historyOrderFlightList = new ArrayList<>();
        for (Object[]row : results) {
            HistoryOrderFlight historyOrderFlight=new HistoryOrderFlight(
                    (Integer)row[0],
                    (String)row[1],
                    (String) row[2],
                    (LocalDateTime) row[3],
                    (LocalDateTime) row[4],
                    (String) row[5],
                    (String) row[6]
            );
            historyOrderFlightList.add(historyOrderFlight);
        }
        return historyOrderFlightList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }


    private String flightDate;

}
