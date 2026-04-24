package com.example.seatService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.seatService.Entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    public List<Seat> findByShowId(String showId);

}
