package com.example.bookingService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "booking_status")
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String bookingId;

    String seatId;
    String showId;
    String userId;

}
