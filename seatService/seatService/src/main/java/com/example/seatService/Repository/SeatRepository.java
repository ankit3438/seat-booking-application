package com.example.seatService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seatService.Entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    public List<Seat> findByShowId(String showId);

}
