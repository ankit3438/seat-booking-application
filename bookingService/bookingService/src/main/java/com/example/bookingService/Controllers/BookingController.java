package com.example.bookingService.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookingService.Services.BookingService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/bookSeat")
    public ResponseEntity<String> postMethodName(@PathVariable String seatId) {
        //TODO: process POST request
        return new ResponseEntity<>(bookingService.bookSeat(seatId), HttpStatus.OK);
    }

}
