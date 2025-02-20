package com.vtnq.web.DTOs.HistoryOrder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryOrderHotel {
    private int id;
    private String hotelName;
    private String typeRoom;
    private String CheckInDate;
    private String CheckOutDate;
    private int totalRoom;
    private String formatDate(LocalDate time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(time);
    }
    public HistoryOrderHotel(int id, String hotelName, String typeRoom, LocalDate checkInDate, LocalDate checkOutDate) {
        this.id = id;
        this.hotelName = hotelName;
        this.typeRoom = typeRoom;
        CheckInDate = formatDate(checkInDate);
        CheckOutDate = formatDate(checkOutDate);
    }
    public static List<HistoryOrderHotel>mapHistoryOrderHotel(List<Object[]>results){
        List<HistoryOrderHotel>list = new ArrayList<>();
        for (Object[]row : results) {
            HistoryOrderHotel hotel=new HistoryOrderHotel(
                    (Integer) row[0], // id
                    (String) row[1],  // hotelName
                    (String) row[2],  // roomType
                    (LocalDate) row[3],    // checkInDate
                    (LocalDate) row[4]
            );
            list.add(hotel);
        }
        return list;
    }
    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(String typeRoom) {
        this.typeRoom = typeRoom;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }
}
