package com.example.seatService.Service;

import java.util.Optional;

import org.aspectj.weaver.Shadow;
import org.springframework.stereotype.Service;

import com.example.seatService.Dto.ShowDto;
import com.example.seatService.Entity.Shows;
import com.example.seatService.Repository.ShowRepository;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Shows addShow(ShowDto showDto) {
        // Logic to retrieve show details by its ID
        try{
            Shows show= new Shows();
            show.setShowId(showDto.getShowId());
            show.setMovieName(showDto.getMovieName());  
            show.setShowTime(showDto.getShowTime());
            show.setAvailableSeats(0);
            show.setTotalSeats(0);

            return showRepository.save(show);

        }catch(Exception e){
            throw new RuntimeException("Failed to add show: " + e.getMessage());
        }
    }

    public Shows getShows(String id){
        try{
            return showRepository.findById(id).orElse(null);
        }
        catch(Exception e){
            throw new RuntimeException("No Show available for the mentioned show id"+ e.getMessage());
        }
    }

}
