package com.cooksys.core.dao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import javax.transaction.Transactional;



import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import sun.util.logging.resources.logging;



import com.cooksys.core.dao.UserFlightDAO;
import com.cooksys.core.models.Flight;
import com.hibernate.BookedFlight;
import com.hibernate.User;
import com.hibernate.UserFlight;

@Component
@Transactional
public class UserFlightService {

	@Autowired
	UserFlightDAO userFlightDAO;
	
	@Autowired
	FlightSearch flightSearch;
	
	Logger log = LoggerFactory.getLogger(UserFlightService.class);
	
	public List<Flight> getBookedFlightPath(User user){
		List<UserFlight> userFlight = userFlightDAO.getUserFlightByUser(user);
		List<Flight> userFlights = new ArrayList<Flight>();
		
		userFlight.forEach(f->{
			userFlights.add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
		});
		
		return userFlights;
	}
	
	/**
	 * Gets User Booked Flight Paths
	 * @param user
	 * @return
	 */
	public Map<Integer,List<Flight>> getBookedFlightsByUser(User user){
		Set<UserFlight> set = user.getUserFlights();
		List<UserFlight> userFlights = new ArrayList<UserFlight>();
		Map<Integer,List<Flight>> flightPath = new HashMap<Integer, List<Flight>>();
		
		int counter = 0;
		int pathCount = 1;
		set.forEach(f->{
			userFlights.add(f);
		});
		log.info("bomb " + userFlights.size());
		for(UserFlight f : userFlights){
			if(counter < userFlights.size()){
				if(counter != userFlights.size()){
				if((f.getOrigin().equals(userFlights.get(counter+1).getOrigin())) && (f.getDestination().equals(userFlights.get(counter+1).getDestination()))){
					if(!flightPath.containsKey(pathCount)){
						flightPath.put(pathCount, new ArrayList<Flight>());
						flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
						log.info("flight path " + pathCount + ": " + f.getBookedFlight().getFlightId());
					}else{
						flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
						log.info("flight path contains " + pathCount + ": " + f.getBookedFlight().getFlightId());
					}
				}else{
					pathCount++;
					if(!flightPath.containsKey(pathCount)){
						flightPath.put(pathCount, new ArrayList<Flight>());
						flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
						log.info("flight path doesnot equal" + pathCount + ": " + f.getBookedFlight().getFlightId());
					}
				}
				}else{
					if((f.getOrigin().equals(userFlights.get(counter+1).getOrigin())) && (f.getDestination().equals(userFlights.get(counter+1).getDestination()))){
						if(!flightPath.containsKey(pathCount)){
							flightPath.put(pathCount, new ArrayList<Flight>());
							flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
							log.info("flight path " + pathCount + ": " + f.getBookedFlight().getFlightId());
						}else{
							flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
							log.info("flight path contains " + pathCount + ": " + f.getBookedFlight().getFlightId());
						}
				}
				log.info(counter + " inside 1");
				counter++; 
			}}else{
				if((f.getOrigin().equals(userFlights.get(counter).getOrigin())) && (f.getDestination().equals(userFlights.get(counter).getDestination()))){
					if(!flightPath.containsKey(pathCount)){
						flightPath.put(pathCount, new ArrayList<Flight>());
						flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
						log.info("flight path " + pathCount + ": " + f.getBookedFlight().getFlightId());
					}
					flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
					log.info("flight path " + pathCount + ": " + f.getBookedFlight().getFlightId());
				}else{
					pathCount++;
					if(!flightPath.containsKey(pathCount)){
						flightPath.put(pathCount, new ArrayList<Flight>());
						flightPath.get(pathCount).add(flightSearch.getFlightByFlightId(f.getBookedFlight().getFlightId()));
						log.info("flight path " + pathCount + ": " + f.getBookedFlight().getFlightId());
					}
				}
				log.info(counter + "inside 2");
				counter++;
			}
		}
		
		return flightPath;

	}
	
	/**
	 * get all UserFlight
	 * @return
	 */
	public List<UserFlight> getAllUserFlight() {
		return userFlightDAO.getAllUserFlight();
	}

	/**
	 * get UserFlight by User
	 * @param user
	 * @return
	 */
	public List<UserFlight> getUserFlightByUser(User user) {
		return userFlightDAO.getUserFlightByUser(user);
	}
	
	/**
	 * create UserFlight 
	 * @param user
	 * @param bookedFlight
	 * @param origin
	 * @param destination
	 */
	public void createUserFlight(User user, BookedFlight bookedFlight, String origin, String destination) {
		UserFlight userFlight = new UserFlight(bookedFlight, user, origin, destination);
		userFlightDAO.createUserFlight(userFlight);
	}

	/**
	 * Delete UserFlight
	 * @param userFlight
	 */
	public void deleteUserFlight(UserFlight userFlight) {
		userFlightDAO.deleteUserFlight(userFlight);
	}
}
