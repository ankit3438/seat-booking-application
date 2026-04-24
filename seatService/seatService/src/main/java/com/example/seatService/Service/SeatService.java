package com.example.seatService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.seatService.Dto.SeatDto;
import com.example.seatService.Entity.Seat;
import com.example.seatService.Repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowService showService;


    public Seat addSeat(SeatDto seatDto) {
        // Logic to book a seat
        Seat seat = new Seat();
        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setShowId(seatDto.getShowId());
        seat.setAvailable(true);
        seat.setSeatId(seatDto.getSeatId());

        //updating count of available seats in the show
        showService.getShows(seatDto.getShowId()).setAvailableSeats(showService.getShows(seatDto.getShowId()).getAvailableSeats() + 1);
        showService.getShows(seatDto.getShowId()).setTotalSeats(showService.getShows(seatDto.getShowId()).getTotalSeats() + 1);

        return seatRepository.save(seat);
    }

    public List<Seat> getSeatByShowId(String Show_id) {
        // Logic to retrieve a seat by its ID
        return seatRepository.findByShowId(Show_id);
    }


}
