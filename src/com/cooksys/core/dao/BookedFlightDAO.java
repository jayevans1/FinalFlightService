package com.cooksys.core.dao;

import java.util.List;

import com.hibernate.BookedFlight;

public interface BookedFlightDAO {
	
	public List<BookedFlight> getAllBookedFlight();
	
	public BookedFlight getFlightByFlightId(int flightId);
	
	public void createBookedFlight(BookedFlight bookedFlight);
	
	public void deleteBookedFlight(BookedFlight bookedFlight);
	
	public void updateBookedFlight(BookedFlight bookedFlight);
}
