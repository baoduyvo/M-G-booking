package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    @Query("SELECT a from Seat a where a.idFlight.id= :flightId")
    List<Seat>FindSeatByFlight(int flightId);
    @Query("SELECT  CASE WHEN COUNT(a)>0 THEN TRUE ELSE FALSE END FROM Seat a where a.idFlight.id = :flightId")
    boolean existsSeatFlight(int flightId);
}
