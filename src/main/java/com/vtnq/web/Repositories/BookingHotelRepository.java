package com.vtnq.web.Repositories;

import com.vtnq.web.DTOs.HistoryOrder.HistoryOrderHotel;
import com.vtnq.web.Entities.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingHotelRepository extends JpaRepository<BookingRoom, Integer> {

}
