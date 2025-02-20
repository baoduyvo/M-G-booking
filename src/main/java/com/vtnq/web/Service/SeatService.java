package com.vtnq.web.Service;

import com.vtnq.web.Controllers.Seat.SeatDTO;
import com.vtnq.web.Entities.Seat;

import java.util.List;

public interface SeatService {
    public List<SeatDTO>FindSeatByFlight(int idFlight);
    public boolean existSeatByFlight(int idFlight);
}
