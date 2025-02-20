package com.vtnq.web.Service;

import com.vtnq.web.Controllers.Seat.SeatDTO;
import com.vtnq.web.Entities.Seat;
import com.vtnq.web.Repositories.SeatRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
@Service
public class SeatServiceImplement implements SeatService {
  @Autowired
  private SeatRepository seatRepository;
  @Autowired
  private ModelMapper modelMapper;


    @Override
    public List<SeatDTO> FindSeatByFlight(int idFlight) {
        try {
            List<SeatDTO> seats = modelMapper.map(seatRepository.FindSeatByFlight(idFlight), new TypeToken<List<SeatDTO>>(){}.getType());

            return seats;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existSeatByFlight(int idFlight) {
        try {
            return seatRepository.existsSeatFlight(idFlight);
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

}
