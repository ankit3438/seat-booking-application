package com.example.seatService.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seatService.Dto.ShowDto;
import com.example.seatService.Entity.Shows;
import com.example.seatService.Service.ShowService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/addShow")
    public ResponseEntity<ShowDto> postMethodName(@RequestBody ShowDto showDto) {
        //TODO: process POST request
        try {
            Shows show = showService.addShow(showDto);
            ShowDto responseDto = new ShowDto();
            responseDto.setShowId(show.getShowId());
            responseDto.setMovieName(show.getMovieName());
            responseDto.setShowTime(show.getShowTime());
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
        
    }

    @GetMapping("/getShow/{id}")
    public ResponseEntity<Shows> getShows(@PathVariable Long id) {
        Shows show = showService.getShows(id);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(show);
    }
    
    
    
}
