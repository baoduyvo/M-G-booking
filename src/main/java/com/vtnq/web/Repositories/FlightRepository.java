package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.Booking.BookingListFly;
import com.vtnq.web.DTOs.Flight.FlightListDTO;
import com.vtnq.web.DTOs.Flight.ResultFlightDTO;
import com.vtnq.web.Entities.City;
import com.vtnq.web.Entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    @Query("select new com.vtnq.web.DTOs.Flight.FlightListDTO(f.id,f.airline.name,f.departureAirport.city.name,f.arrivalAirport.city.name,f.departureTime,f.arrivalTime)" +
            " from Flight f where f.departureAirport.city.country.id = :id")
    List<FlightListDTO> findFlightListDTO(@Param("id") int id);

    @Query("select count(a) from Flight  a where a.airline.countryId = :id")
    int CountFlight(@Param("id") int id);

    @Query("SELECT MIN(G.price) FROM Flight f Join Seat G on f.id=G.idFlight.id " +
            "where  DATE(f.departureTime) = :departureTime " +
            "and f.departureAirport.id = :departureAirport " +
            "and f.arrivalAirport.id = :arrivalAirport " +
            "and G.type = :TypeFlight" +
            " and (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople and f.departureTime > :CurrentDate")
    public BigDecimal FindPrice(@Param("departureAirport") int departureAirport,
                                @Param("arrivalAirport") int arrivalAirport,
                                @Param("departureTime") LocalDate departureTime,
                                @Param("TypeFlight") String TypeFlight,
                                @Param("totalPeople") int totalPeople,
                                @Param("CurrentDate")LocalDateTime CurrentDate);

    @Query("SELECT new com.vtnq.web.DTOs.Flight.ResultFlightDTO(" +
            "f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime, " +
            "min(f.price), c.name, f.departureTime, f.arrivalTime, f.departureTime, f.arrivalTime, " +
            "f.departureAirport.name, f.airline.id, f.arrivalAirport.name) " +
            "FROM Flight f " +
            "JOIN Airport a ON a.id = f.departureAirport.id " +
            "JOIN Airport b ON b.id = f.arrivalAirport.id " +
            "JOIN Airline c ON c.id = f.airline.id " +
            "JOIN Picture d ON c.id = d.airlineId " +
            "LEFT JOIN Seat G ON f.id = G.idFlight.id " +
            "JOIN City e ON e.id = b.city.id " +
            "WHERE f.departureAirport.id = :departureAirport " +
            "AND f.arrivalAirport.id = :arrivalAirport " +
            "AND DATE(f.departureTime) = :departureTime " +
            "AND G.type = :TypeFlight " +
            "AND G.status = 0 " +
            "AND d.isMain = true  and f.departureTime > :CurrentDate " +
            "GROUP BY f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime, " +
            "c.name, f.departureAirport.name, f.airline.id, f.arrivalAirport.name " +
            "ORDER BY min(f.price) ASC")

    List<ResultFlightDTO> findResulFlightAndHotel(
            @Param("departureAirport") int departureAirport,
            @Param("arrivalAirport") int arrivalAirport,
            @Param("departureTime") LocalDate departureTime,
            @Param("TypeFlight") String TypeFlight,
            @Param("CurrentDate") LocalDateTime CurrentDate);


    @Query("select distinct new com.vtnq.web.DTOs.Flight.ResultFlightDTO(f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime,f.price, c.name, f.departureTime, f.arrivalTime, f.departureTime, f.arrivalTime, f.departureAirport.name, f.airline.id, f.arrivalAirport.name) " +
            "from Flight f " +
            "join Airport a on a.id = f.departureAirport.id " +
            "join Airport b on b.id = f.arrivalAirport.id " +
            "join Airline c on c.id = f.airline.id " +
            "join Picture d on c.id = d.airlineId " +
            "join Seat G on f.id = G.idFlight.id " +
            "where f.id = :id and d.isMain=true and G.status=0  " +
            "group by f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime, c.name, f.departureAirport.name, f.airline.id, f.arrivalAirport.name ")
    ResultFlightDTO FindByFlightId(int id);

    @Query("SELECT distinct new com.vtnq.web.DTOs.Flight.ResultFlightDTO(f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime, " +
            "f.price, c.name, f.departureTime, f.arrivalTime, f.departureTime, f.arrivalTime, f.departureAirport.name, f.airline.id, f.arrivalAirport.name) " +
            "FROM Flight f " +
            "JOIN Airport a ON a.id = f.departureAirport.id " +
            "JOIN Airport b ON b.id = f.arrivalAirport.id " +
            "JOIN Airline c ON c.id = f.airline.id " +
            "JOIN Picture d ON c.id = d.airlineId " +
            "LEFT JOIN Seat G ON f.id = G.idFlight.id " +
            "JOIN City e ON e.id = b.city.id " +
            "WHERE f.departureAirport.id = :departureAirport " +
            "AND f.arrivalAirport.id = :arrivalAirport " +
            "AND DATE(f.departureTime) = :departureTime " +
            "AND G.type = :TypeFlight And G.status=0  " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople AND f.departureTime > :currentTime ")
    List<ResultFlightDTO> findFlightsByAirportsAndDepartureTime(
            @Param("departureAirport") int departureAirport,
            @Param("arrivalAirport") int arrivalAirport,
            @Param("departureTime") LocalDate departureTime,
            @Param("TypeFlight") String TypeFlight, @Param("totalPeople") int totalPeople, @Param("currentTime")LocalDateTime currentTime);

    @Query("select min(f.price) from Flight f " +
            "join Airport a on a.id = f.departureAirport.id " +
            "join Airport b ON b.id=f.arrivalAirport.id " +
            "join Airline c on c.id=f.airline.id " +
            "join Picture d on c.id=d.airlineId " +
            "left join Seat G on f.id=G.idFlight.id " +
            "JOIN City e On e.id=b.city.id " +
            "where f.departureAirport.id = :departureAirport " +
            "AND f.arrivalAirport.id = :arrivalAirport " +
            "AND DATE(f.departureTime)= :departureTime " +
            "AND G.type = :TypeFlight And G.status=0 " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople and f.departureTime > :CurrentDate")
    BigDecimal FindMinPriceDeparture(@Param("departureAirport") int departureAirport,
                                     @Param("arrivalAirport") int arrivalAirport,
                                     @Param("departureTime") LocalDate departureTime,
                                     @Param("TypeFlight") String TypeFlight, @Param("totalPeople") int totalPeople,@Param("CurrentDate")LocalDateTime CurrentDate);

    @Query("select max (f.price) from Flight f " +
            "join Airport a on a.id = f.departureAirport.id " +
            "join Airport b ON b.id=f.arrivalAirport.id " +
            "join Airline c on c.id=f.airline.id " +
            "join Picture d on c.id=d.airlineId " +
            "left join Seat G on f.id=G.idFlight.id " +
            "JOIN City e On e.id=b.city.id " +
            "where f.departureAirport.id = :departureAirport " +
            "AND f.arrivalAirport.id = :arrivalAirport " +
            "AND DATE(f.departureTime)= :departureTime " +
            "AND G.type = :TypeFlight And G.status=0 and f.departureTime > :CurrentDate " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople")
    BigDecimal FindMaxPriceDeparture(@Param("departureAirport") int departureAirport,
                                     @Param("arrivalAirport") int arrivalAirport,
                                     @Param("departureTime") LocalDate departureTime,
                                     @Param("TypeFlight") String TypeFlight, @Param("totalPeople") int totalPeople,@Param("CurrentDate")LocalDateTime CurrentDate);

    @Query("SELECT distinct  new com.vtnq.web.DTOs.Flight.ResultFlightDTO(f.id,d.imageUrl,f.arrivalAirport.city.name,f.arrivalTime,f.departureTime," +
            "f.price,c.name,f.departureTime,f.arrivalTime,f.departureTime,f.arrivalTime,f.departureAirport.name,f.airline.id,f.arrivalAirport.name) FROM Flight f " +
            "JOIN Airport a on a.id=f.departureAirport.id " +
            "JOIN Airport b on b.id=f.arrivalAirport.id " +
            "JOIN Airline c on c.id=f.airline.id " +
            "JOIN Picture d on c.id=d.airlineId " +
            "left Join Seat G on f.id=G.idFlight.id " +
            "join City e on e.id=b.city.id " +
            "WHERE f.departureAirport.id= :departureAirport " +
            "AND f.arrivalAirport.id = :arrivalAirport " +
            "AND DATE(f.departureTime) = :departureTime " +
            "And G.type = :TypeFlight and G.status=0 " +
            "And Date(f.arrivalTime) = :arrivalTime and f.departureTime > :CurrentDate")
    List<ResultFlightDTO> SearchFindFlightAll(@Param("departureAirport") int departureAirport,
                                              @Param("arrivalAirport") int arrivalAirport,
                                              @Param("departureTime") LocalDate departureTime,
                                              @Param("arrivalTime") LocalDate arrivalTime,
                                              @Param("TypeFlight") String TypeFlight,@Param("CurrentDate")LocalDateTime CurrentDate);

    @Query("SELECT  new com.vtnq.web.DTOs.Booking.BookingListFly(f.id,d.imageUrl,a.name,c.name,f.departureTime,f.arrivalTime,f.airline.name,f.arrivalTime,f.departureTime,f.departureAirport.city.name,f.arrivalAirport.city.name) FROM Flight f " +
            "JOIN Airport a on a.id=f.departureAirport.id " +
            "JOIN Airport b on b.id=f.arrivalAirport.id " +
            "JOIN Airline c on c.id=f.airline.id " +
            "JOIN Picture d on c.id=d.airlineId " +
            "WHERE f.id = :id")
    BookingListFly findFlightsByAirportsAndDepartureTime(@Param("id") int id);

    @Query("SELECT  new com.vtnq.web.DTOs.Flight.ResultFlightDTO(f.id,d.imageUrl,f.arrivalAirport.city.name,f.arrivalTime,f.departureTime,f.price,c.name,f.departureTime,f.arrivalTime,f.departureTime,f.arrivalTime,f.departureAirport.name,f.airline.id,f.arrivalAirport.name) from Flight f " +
            "join Airport a on a.id=f.departureAirport.id " +
            "join Airport b on b.id=f.arrivalAirport.id " +
            "join Airline c on c.id=f.airline.id " +
            "join Picture d on d.airlineId=c.id " +
            "join Seat e on e.idFlight.id =f.id " +
            "where f.departureAirport.id = :arrivalAirport " +
            "and f.arrivalAirport.id = :departureAirport " +
            "and DATE(f.departureTime) = :arrivalTime and e.status=0 " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople " +
            "and e.type = :TypeFlight and f.departureTime > :CurrentDate " +
            "group by f.id, d.imageUrl, f.arrivalAirport.city.name, f.arrivalTime, f.departureTime, e.price, c.name, f.departureAirport.name, f.airline.id, f.arrivalAirport.name")
    List<ResultFlightDTO> FindArrivalTimeFlights(@Param("departureAirport") int departureAirport,
                                                 @Param("arrivalAirport") int arrivalAirport,
                                                 @Param("arrivalTime") LocalDate arrivalTime,
                                                 @Param("TypeFlight") String TypeFlight,@Param("totalPeople") int totalPeople,@Param("CurrentDate")LocalDateTime CurrentDate);
    @Query("SELECT  min(f.price) from Flight f " +
            "join Airport a on a.id=f.departureAirport.id " +
            "join Airport b on b.id=f.arrivalAirport.id " +
            "join Airline c on c.id=f.airline.id " +
            "join Picture d on d.airlineId=c.id " +
            "left join Seat e on e.idFlight.id =f.id " +
            "where f.departureAirport.id = :arrivalAirport " +
            "and f.arrivalAirport.id = :departureAirport " +
            "and DATE(f.departureTime) = :arrivalTime and e.status=0 " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople " +
            "and e.type = :TypeFlight and d.isMain=true and f.departureTime > :CurrentDate")
    BigDecimal MinArrivalTime(@Param("departureAirport") int departureAirport,
                              @Param("arrivalAirport") int arrivalAirport,
                              @Param("arrivalTime") LocalDate arrivalTime,
                              @Param("TypeFlight") String TypeFlight,@Param("totalPeople") int totalPeople,@Param("CurrentDate") LocalDateTime CurrentDate);
    @Query("SELECT  max(f.price) from Flight f " +
            "join Airport a on a.id=f.departureAirport.id " +
            "join Airport b on b.id=f.arrivalAirport.id " +
            "join Airline c on c.id=f.airline.id " +
            "join Picture d on d.airlineId=c.id " +
            "join Seat e on e.idFlight.id =f.id " +
            "where f.departureAirport.id = :arrivalAirport " +
            "and f.arrivalAirport.id = :departureAirport " +
            "and DATE(f.departureTime) = :arrivalTime and e.status=0 " +
            "AND (SELECT COUNT(seat) FROM Seat seat WHERE seat.idFlight.id = f.id AND seat.type = :TypeFlight) >= :totalPeople " +
            "and e.type = :TypeFlight and d.isMain=true and f.departureTime > :CurrentDate")
    BigDecimal MaxArrivalTime(@Param("departureAirport") int departureAirport,
                              @Param("arrivalAirport") int arrivalAirport,
                              @Param("arrivalTime") LocalDate arrivalTime,
                              @Param("TypeFlight") String TypeFlight,@Param("totalPeople") int totalPeople,@Param("CurrentDate") LocalDateTime CurrentDate);
    @Query("select c from City c " +
            "join Flight f on f.arrivalAirport.city.id = c.id " +
            "group by c.id " +
            "order by count(f) desc")
    List<City> findTopCities();
}