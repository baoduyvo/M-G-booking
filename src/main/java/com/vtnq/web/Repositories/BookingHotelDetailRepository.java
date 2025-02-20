package com.vtnq.web.Repositories;

import com.vtnq.web.Entities.BookingRoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingHotelDetailRepository extends JpaRepository<BookingRoomDetail,Integer> {
}
