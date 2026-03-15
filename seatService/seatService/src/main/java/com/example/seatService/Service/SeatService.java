package com.example.seatService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seatService.Entity.Seat;
import com.example.seatService.Repository.SeatRepository;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(Seat seat) {
        // Logic to book a seat
        return seatRepository.save(seat);
    }

    public List<Seat> getSeatByShowId(String Show_id) {
        // Logic to retrieve a seat by its ID
        return seatRepository.findByShowId(Show_id);
    }


}
