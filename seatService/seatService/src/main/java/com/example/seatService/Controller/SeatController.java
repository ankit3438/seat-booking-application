package com.example.seatService.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seatService.Dto.SeatDto;
import com.example.seatService.Entity.Seat;
import com.example.seatService.Service.SeatService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    @PostMapping("/addSeats")
    public ResponseEntity<?> addSeats(@RequestBody SeatDto seatDto) {
        //TODO: process POST request
        try{
            Seat seat = new Seat();
            seat.setSeatNumber(seatDto.getSeatNumber());
            seat.setShowId(seatDto.getShowId());
            seat.setAvailable(true);

            return new ResponseEntity<>(seatService.addSeat(seat), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("error while adding seat: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/viewSeat/{showId}")
    public ResponseEntity<List<Seat>> getSeatByShowID(@PathVariable String showId) {
        return new ResponseEntity<>(seatService.getSeatByShowId(showId), HttpStatus.OK);
    }
    

}
