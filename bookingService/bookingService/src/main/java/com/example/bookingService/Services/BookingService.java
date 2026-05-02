package com.example.bookingService.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.bookingService.Dto.Seat;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookingService {

    private final RestTemplate restTemplate;

    public String bookSeat(String seatId) {

        //1. get seat details from seat service
        String seatServiceUrl = "http://localhost:8082/Seats/getSeat/" + seatId;
        Seat seat = restTemplate.getForObject(seatServiceUrl, Seat.class);

        return "hello";
    }

}
