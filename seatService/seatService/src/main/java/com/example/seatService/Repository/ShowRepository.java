package com.example.seatService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.seatService.Entity.Shows;

@Repository
public interface ShowRepository extends JpaRepository<Shows, Long> {

}
