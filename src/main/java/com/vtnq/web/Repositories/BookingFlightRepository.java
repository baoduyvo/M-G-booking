package com.vtnq.web.Repositories;

import com.vtnq.web.Controllers.HistoryOrder;
import com.vtnq.web.DTOs.HistoryOrder.HistoryOrderFlight;
import com.vtnq.web.Entities.BookingFlight;
import com.vtnq.web.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingFlightRepository extends JpaRepository<BookingFlight, Integer> {
    @Query("select distinct a.id,a.seat.index,a.flight.flightCode,a.flight.arrivalTime,a.flight.departureTime,a.flight.departureAirport.name,a.flight.arrivalAirport.name from BookingFlightDetail a " +
            "where a.bookingFlight.id = :id and (:flightCode IS null or a.flight.flightCode like %:flightCode%) " +
            "and (:departureTime IS NULL or Date(a.flight.departureTime) =:departureTime) " +
            "and (:ArrivalTime IS NULL or Date(a.flight.arrivalTime) =:ArrivalTime)" +
            "order by a.id ASC limit :size offset :offset ")
    List<Object[]>FindFlightByUser(@Param("id") int id, @Param("size")int size, @Param("offset")int offset,@Param("flightCode")String flightCode,@Param("departureTime") LocalDate departureTime,
                                   @Param("ArrivalTime")LocalDate arrivalTime);
    @Query("select a from Seat a join BookingFlightDetail b on a.id=b.seat.id where b.bookingFlight.id = :id")
    List<Seat>FindSeatByBookingFlight(int id);
}