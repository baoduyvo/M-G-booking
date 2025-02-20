package com.vtnq.web.DTOs.HistoryOrder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryBooking {
    private int idFlight;
    private int idHotel;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    private BigDecimal totalPrice;
    private String CreateAt;
    private String bookingCode;

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    private String formatInstant(Instant instant) {
        // Convert Instant to ZonedDateTime with your desired time zone
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC")); // Use system default or specify another zone

        // Define the format: "dd MM yyyy HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm");

        // Format the ZonedDateTime and return the string
        return zonedDateTime.format(formatter);
    }
    public HistoryBooking(Integer idFlight,Integer idHotel,Instant createAt,BigDecimal totalPrice,String bookingCode) {
        this.idFlight = idFlight != null ? idFlight : 0;
        this.idHotel = idHotel != null ? idHotel : 0;
        this.CreateAt = formatInstant(createAt);
        this.totalPrice = totalPrice;
        this.bookingCode = bookingCode;
    }
    public static List<HistoryBooking> getHistoryBookings(List<Object[]>results) {
        List<HistoryBooking> historyBookings = new ArrayList<>();
        for (Object[]row : results) {
            HistoryBooking historyBooking=new HistoryBooking(
                    (Integer)row[0],
                    (Integer)row[1],
                    (Instant)row[2],
                    (BigDecimal) row[3],
                    (String) row[4]
            );
            historyBookings.add(historyBooking);
        }
        return historyBookings;
    }
}
