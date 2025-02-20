package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.Booking;
import com.vtnq.web.Entities.BookingFlightDetail;
import com.vtnq.web.Entities.BookingRoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select  a from Booking a join BookingFlight b on b.id=a.bookingFlight.id join BookingFlightDetail c on c.bookingFlight.id=b.id " +
            "where c.flight.airline.countryId= :id")
    List<Booking>findBookingByCountry(int id);
    @Query("select count(a) from Booking a join BookingFlight b on b.id=a.bookingFlight.id join BookingFlightDetail c on c.bookingFlight.id=b.id " +
            "where c.flight.airline.countryId= :id")
    int CountBooking(int id);
    @Query("select a from BookingFlightDetail a join BookingFlight b on a.bookingFlight.id=b.id " +
            "join Booking c on c.bookingFlight.id=b.id where c.id = :id")
    List<BookingFlightDetail>FindBookingByFlight(int id);
    @Query("select b.totalPrice from Booking a join BookingFlight b on b.id=a.bookingFlight.id where a.id = :id")
    public BigDecimal getBookingTotalPrice(int id);
    @Query("select a from BookingRoomDetail a join BookingRoom b on a.bookingRoom.id=b.id " +
            "join Booking c on c.bookingRoom.id=b.id where c.id = :id")
        public List<BookingRoomDetail>getBookingRoomDetails(int id);
    @Query("select a.totalPrice from BookingRoom a join BookingRoomDetail b on b.bookingRoom.id=a.id " +
            "join Booking c on c.bookingRoom.id=a.id where c.id =  :id")
    public BigDecimal getBookingHotelPrice(int id);
    @Query("select a from Booking a where a.userId = :id")
    public List<Booking>FindBookingByUserId(int id);
    @Query("select a.bookingFlight.id as idFlight,a.bookingRoom.id as idHotel,a.createdAt as CreateAt,a.totalPrice as totalPrice,a.bookingCode as bookingCode from Booking a where a.userId= :id and (:booking IS NULL or a.bookingCode like %:booking%) order by a.id asc limit :size offset :offset")
    List<Object[]>FindBookingByAccount(@Param("id") int id, @Param("size")int size, @Param("offset")int offset,@Param("booking") String booking);
    }