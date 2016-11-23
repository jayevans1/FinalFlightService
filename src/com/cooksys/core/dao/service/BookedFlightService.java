package com.cooksys.core.dao.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cooksys.core.dao.BookedFlightDAO;
import com.cooksys.core.dao.UserDAO;
import com.cooksys.core.dao.UserFlightDAO;
import com.cooksys.core.models.Flight;
import com.hibernate.BookedFlight;
import com.hibernate.User;

@Component
@Transactional
public class BookedFlightService {

	@Autowired
	BookedFlightDAO bookedFlightDAO;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserFlightService userFlightService;
	
	/**
	 * checks if a Flight is full
	 * @param flightId
	 * @return
	 */
	public boolean isFlightFull(int flightId){
		BookedFlight bookedFlight = bookedFlightDAO.getFlightByFlightId(flightId);
		if(bookedFlight == null){
			return false;
		}
		if(bookedFlight.getAvailableSeat() >= 5){
			return true;
		}
		return false;
	}
	
	/**
	 * Check all the Flights if there is available seats.
	 *  If one does not have available seats, return false,
	 *  
	 * @param flights
	 * @return
	 */
	public boolean isAllFlightAvailable(List<Flight> flights){
		boolean isFlightPathAvailable = true;
		
		for(Flight f : flights){
			if(isFlightFull(f.getFlightId())){
				isFlightPathAvailable = false;
				break;
			}
		}
		
		return isFlightPathAvailable;
	}
	
	/**
	 * Checks database to see if this flight has been created 
	 * @param flightId
	 * @return
	 */
	public boolean isFlightCreated(int flightId){
		BookedFlight bookedFlight = bookedFlightDAO.getFlightByFlightId(flightId);
		if(bookedFlight == null){
			return false;
		}
		return true;
	}
	
	/**
	 * gets the flights available seats
	 * @param flightId
	 * @return
	 */
	public int getBookedFlightSeats(int flightId){
		BookedFlight bookedFlight = bookedFlightDAO.getFlightByFlightId(flightId);
		return bookedFlight.getAvailableSeat();
	}
	
	/**
	 * Book All the Flights in the flight path chosen by the user
	 * @param username
	 * @param flights
	 * @param origin
	 * @param destination
	 */
	public void doBookedFlight(String username, List<Flight> flights, String origin, String destination){
		User user = userService.getUserByUsername(username);
		if(isAllFlightAvailable(flights)){
			for(Flight f : flights){
				if(isFlightCreated(f.getFlightId())){
					BookedFlight bookedFlight = bookedFlightDAO.getFlightByFlightId(f.getFlightId());
					bookedFlight.setAvailableSeat(bookedFlight.getAvailableSeat()-1);
					bookedFlightDAO.updateBookedFlight(bookedFlight);
					userFlightService.createUserFlight(user, bookedFlight, origin, destination);
				}else{
					BookedFlight bookedFlight = new BookedFlight(f.getFlightId(), 4, 5);
					bookedFlightDAO.createBookedFlight(bookedFlight);
					userFlightService.createUserFlight(user, bookedFlight, origin, destination);
				}
			}
		}
	}
	
	/**
	 * gets all bookedFlight from the database
	 * @return
	 */
	public List<BookedFlight> getAllBookedFlight() {
		return bookedFlightDAO.getAllBookedFlight();
	}
	
	/**
	 * get a bookedFlight by flightId
	 * @param flightId
	 * @return
	 */
	public BookedFlight getFlightByFlightId(int flightId) {
		return bookedFlightDAO.getFlightByFlightId(flightId);
	}

	/**
	 * create a BookedFlight
	 * @param flightId
	 */
	public void createBookedFlight(int flightId) {
		BookedFlight bookedFlight = new BookedFlight();
		bookedFlight.setFlightId(flightId);
		bookedFlightDAO.createBookedFlight(bookedFlight);
	}

	/**
	 * delete a BookedFlight
	 * @param bookedFlight
	 */
	public void deleteBookedFlight(BookedFlight bookedFlight) {
		bookedFlightDAO.deleteBookedFlight(bookedFlight);
	}
}
