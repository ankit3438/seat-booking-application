package com.example.bookingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingServiceApplication {
	/*
	   booking service --> 
	    1. receives the seat no. from the seat service after checking for shows.
		2. check in while start booking whether its already booked or not 
		3. lock that perticular seat.
		4. proceeds to the payment.
		5. after payment make is_available = false.
		6. make async calls to other services if needed like emails and all.
	 */

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}

}
