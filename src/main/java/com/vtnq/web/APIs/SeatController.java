package com.vtnq.web.APIs;

import com.vtnq.web.Controllers.Seat.SeatDTO;
import com.vtnq.web.Entities.Seat;
import com.vtnq.web.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController("SeatApiController")
@RequestMapping({"api/seat"})
public class SeatController {
    @Autowired
    private SeatService seatService;
    @GetMapping("{id}")
    public ResponseEntity<List<SeatDTO>>DetailSeats(@PathVariable int id){
    try {
    return new ResponseEntity<List<SeatDTO>>(seatService.FindSeatByFlight(id), HttpStatus.OK);
    }catch (Exception e){
        e.printStackTrace();
        return new ResponseEntity<List<SeatDTO>>(HttpStatus.BAD_REQUEST);
    }
    }
    @GetMapping("existBySeat/{id}")
    public ResponseEntity<Boolean>existBySeat(@PathVariable int id){
        try {
            return new ResponseEntity<Boolean>(seatService.existSeatByFlight(id), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
        }
    }
}
