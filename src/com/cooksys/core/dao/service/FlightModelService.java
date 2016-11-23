package com.cooksys.core.dao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cooksys.core.dao.FlightModelDAO;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.cooksys.core.models.Location;
import com.cooksys.core.tools.FlightLinkMap;
import com.mysql.jdbc.log.LogFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Component
public class FlightModelService {
		
	Logger log = LoggerFactory.getLogger(FlightModelService.class);
	
	/**
	 * Get flight by flightId
	 * @param flightId
	 * @return
	 */
	public Flight getFlightByFlightId(int flightId){
		List<Flight>  flights = getAllFlights();
		
		 Flight selectedFlight = null;
		
		 for(Flight f : flights){
			 if(f.getFlightId() == flightId){
					selectedFlight = f;
					break;
				}
		 }
		 return selectedFlight;
	}
	
	/**
	 * Maps All flights to an Origin Map<Location,List<Flight>>
	 * @return
	 */
	public Map<Location, List<Flight>> getAllFlightOriginLocations() {
		List<Flight> flights = getAllFlights();

		Map<Location, List<Flight>> allFlightsMapToOrigin = new HashMap<Location, List<Flight>>();
		//log.info("---------Origin TO Destination---------");
		flights.forEach(f->{
			if (!allFlightsMapToOrigin.containsKey(f.getOrigin())) {
				allFlightsMapToOrigin.put(f.getOrigin(),new ArrayList<Flight>());
				allFlightsMapToOrigin.get(f.getOrigin()).add(f);
				//log.info(f.toString());
			} else {
				allFlightsMapToOrigin.get(f.getOrigin()).add(f);
				//log.info(f.toString());
			}
		});

		return allFlightsMapToOrigin;
	}
	
	/**
	 * Maps All flights to an Destination Map<Location,List<Flight>>
	 * @return
	 */
	public Map<Location, List<Flight>> getAllFlightDestinationLocations() {
		List<Flight> flights = getAllFlights();

		Map<Location, List<Flight>> allFlightsMapToDestination = new HashMap<Location, List<Flight>>();
		//log.info("---------DESTINATION---------");
		flights.forEach(f->{
			if (!allFlightsMapToDestination.containsKey(f.getDestination())) {
				allFlightsMapToDestination.put(f.getDestination(),new ArrayList<Flight>());
				allFlightsMapToDestination.get(f.getDestination()).add(f);
				//log.info(f.toString());
			} else {
				allFlightsMapToDestination.get(f.getDestination()).add(f);
				//log.info(f.toString());
			}
		});

		return allFlightsMapToDestination;
	}

	
	/**
	 * Get flight paths from origin
	 * @param origin
	 * @return
	 */
	public Map<Flight, List<Flight>> getFlightPathFromOrigin(Location origin){
		Map<Flight, List<Flight>> allNextPossibleFlightFromOrigin = mapNextPossibleFlight(origin);
		Map<Flight, List<Flight>> realPossibleFlightFromOrigin = sortNextPossibleFlight(allNextPossibleFlightFromOrigin);
		return realPossibleFlightFromOrigin;
	}
	
	/**
	 * MAY Be finds ONLY 1 Path
	 * @param originFlight
	 * @param destination
	 */
	public Map<Flight,List<Flight>> getFlightRoutes(Flight originFlight, String destination){
		Map<Flight, List<Flight>> flightsFromOrigin = getFlightPathFromOrigin(originFlight.getOrigin());
		log.info(originFlight.toString());
		log.info(Integer.toString(flightsFromOrigin.size()));
		log.info(Integer.toString(flightsFromOrigin.get(originFlight).size()));
		//Flights next possible flights
		List<Flight> originFlightPossiblePaths = flightsFromOrigin.get(originFlight);
		
		 Map<Flight, List<Flight>> linkedFlightsMap = new HashMap<Flight, List<Flight>>();
		log.info("IN and "  + originFlightPossiblePaths.size());
		 //first loop
		 linkedFlightsMap.put(originFlight, new ArrayList<Flight>());
		 originFlightPossiblePaths.forEach(f->{
			 linkedFlightsMap.get(originFlight).add(f);
		 });
		 log.info("OUT");
		 Location currentFligthLocation = originFlight.getOrigin();
		 Location currentFlightDestination = originFlight.getDestination();
		 
		 while(!currentFlightDestination.getCity().equals(destination)){
			 
			 List<String> nextFlightsDestinations = new ArrayList<String>();
			 
			 //second loop
			 originFlightPossiblePaths.forEach(f->{
				 if(!linkedFlightsMap.containsKey(f)){
					 nextFlightsDestinations.add(f.getDestination().getCity());
					 linkedFlightsMap.put(f, new ArrayList<Flight>());
					 getFlightPathFromOrigin(f.getOrigin()).get(f).forEach(ff->{
						 linkedFlightsMap.get(f).add(ff);
					 });
				 }
			 });
			 
			if(nextFlightsDestinations.contains(destination)){
				break;
			}	
		 } 
		 
		 return linkedFlightsMap;
	}
	
	/**
	 * Grab Flights possible for Flight from origin, then
	 * Map Those Flights to Possible Next Flights
	 * @param origin
	 * @return
	 */
	public Map<Flight, List<Flight>> mapNextPossibleFlight(Location origin) {
		List<Flight> flights = getAllFlights();

		List<Flight> flightWithSameOrigin = new ArrayList<Flight>();

		Map<Flight, List<Flight>> nextPossibleFlightMap = new HashMap<Flight, List<Flight>>();
		
		//Get flights with same origin and puts them in a List
		flights.forEach(f->{
			if(f.getOrigin().getCity().equals(origin.getCity()) && f.getOrigin().getState().equals(origin.getState())){
				flightWithSameOrigin.add(f);
			}
		});
		//Each Flight with same origin will now be map to there next possible Flight
		flightWithSameOrigin.forEach(flight->{
			if (!nextPossibleFlightMap.containsKey(flight)) {
				nextPossibleFlightMap.put(flight, new ArrayList<Flight>());
				nextPossibleFlightMap.get(flight).addAll(getAllPossibleNextFlight(flight.getDestination()));
			}
		});
	
		// for (Flight flight : nextPossibleFlightMap.keySet()) {
		//
		// log.info("\n" + flight
		// + " next possible fligth is \n ");
		// for (Flight fl : nextPossibleFlightMap.get(flight)) {
		// log.info(fl);
		// }
		// }

		return nextPossibleFlightMap;
	}
	
	/**
	 * Map all Flight with all its possible Next Flights
	 * 
	 * @return Map<Flight, List<Flight>>
	 */
	public Map<Flight, List<Flight>> mapNextPossibleFlight() {
		List<Flight> flights = getAllFlights();
		Map<Flight, List<Flight>> nextPossibleFlightMap = new HashMap<Flight, List<Flight>>();

		flights.forEach(flight->{
			if (!nextPossibleFlightMap.containsKey(flight)) {
				nextPossibleFlightMap.put(flight, new ArrayList<Flight>());
				nextPossibleFlightMap.get(flight).addAll(
						getAllPossibleNextFlight(flight.getDestination()));
				
				// log.info("Flight ID: " + flight.getFlightId() + " "
				// + flight.getOrigin().getCity() + "," +
				// flight.getOrigin().getState() + " to " +
				// flight.getDestination().getCity() + "," +
				// flight.getDestination().getState() +
				// "'s possible next flights are :\n ");
				// for(Flight f : nextPossibleFlightMap.get(flight)){
				// log.info("Flight ID: " + f.getFlightId() + " " +
				// f.getOrigin().getCity() + "," + f.getOrigin().getState() +
				// " to " + f.getDestination().getCity() + ","
				// +f.getDestination().getState() + "\n\n");
				// }
			}
		});
		return nextPossibleFlightMap;
	}
	
	/**
	 * Grab all possible next flights depending on flight origin and destination
	 * 
	 * @param landingLocation
	 * @return List<Flight>
	 */
	public List<Flight> getAllPossibleNextFlight(Location destinationLocation) {
		List<Flight> flights = getAllFlights();

		List<Flight> possibleNextFlight = new ArrayList<Flight>();

		flights.forEach(flight ->{
			if (flight.getOrigin().getCity().equals(destinationLocation.getCity())) {
				possibleNextFlight.add(flight);
			}
		});

		return possibleNextFlight;
	}
	
	/**
	 * removes flights that are not possible and returns REAL possible flights
	 * 
	 * @param nextPossibleFlight
	 * @return Map<Flight, List<Flight>>
	 */
	public Map<Flight, List<Flight>> sortNextPossibleFlight(Map<Flight, List<Flight>> nextPossibleFlight) {
		
		Set<Flight> flights = nextPossibleFlight.keySet();

		Map<Flight, List<Flight>> tempMap = new HashMap<Flight, List<Flight>>();
		List<Flight> tempList = new ArrayList<Flight>();

		flights.forEach(f->{
			Iterator<Flight> ite = nextPossibleFlight.get(f).iterator();
			tempList.clear();
			while (ite.hasNext()) {
				Flight ff = ite.next();
				if (((f.getDeparture() + f.getEta()) > ff.getDeparture())) {
					ite.remove();
				} else {
					tempList.add(ff);
				}
			}
			if (!tempMap.containsKey(f)) {
				tempMap.put(f, new ArrayList<Flight>());
				tempMap.get(f).addAll(tempList);
				// for (Flight flight : tempMap.keySet()) {
				//
				// log.info("\n" + flight
				// + " next possible fligth is \n ");
				// for (Flight fl : tempMap.get(flight)) {
				// log.info(fl);
				// }
				// }
			}
		});		
		return tempMap;
	}
	
	/**
	 * Get all flights flying from origin
	 * @param origin
	 * @return
	 */
	public List<Flight> getAllOriginFlight(Location origin){
		List<Flight> flight = getAllFlights();
		List<Flight> flightsFromOriginFlights = new ArrayList<Flight>();
		flight.forEach(f->{
			if(f.getOrigin().getCity().equals(origin.getCity())){
				flightsFromOriginFlights.add(f);
			}
		});
		return flightsFromOriginFlights;
	}
	
	/**
	 * Get all flights flying to destination
	 * @param destination
	 * @return
	 */
	public List<Flight> getAllDestinationFlight(Location destination){
		List<Flight> flight = getAllFlights();
		
		List<Flight> flightsToDestinationFlights = new ArrayList<Flight>();
		
		flight.forEach(f->{
			if(f.getDestination().getCity().equals(destination.getCity()) && f.getDestination().getState().equals(destination.getState())){
				flightsToDestinationFlights.add(f);
			}
		});
		return flightsToDestinationFlights;
	}
	
	/**
	 * Checks All flights to see if originCity and destinationCity exist
	 * @param originCity
	 * @param destinationCity
	 * @return
	 */
	public boolean doesFlightPathExist(String originCity, String destinationCity){
		List<Flight> flights = getAllFlights();
		
		boolean isOriginCity = false;
		boolean isDestinationCity = false;
		boolean isFlightPathExist;
		
		for(Flight f : flights){
			if(f.getOrigin().getCity().equals(originCity)){
				isOriginCity = true;
			}
			if(f.getDestination().getCity().equals(destinationCity)){
				isDestinationCity = true;
			}
		}
		
		if(isOriginCity && isDestinationCity)
			isFlightPathExist = true;
		else
			isFlightPathExist = false;
		
		return isFlightPathExist;
	}
	/**
	 * Grabs flight model from web service and returns it
	 * @return
	 */
	public FlightModel getFlightModel() {
		Client client = Client.create();
		WebResource resource = client
				.resource("http://localhost:8080/FinalInstructorWebService/getFlightModel");
		ClientResponse response = resource.accept("application/xml").get(
				ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		FlightModel flightModel = resource.get(new GenericType<FlightModel>() {
		});
		return flightModel;
	}
	
	/**
	 * Get all flights available from the flightModel
	 * @return
	 */
	public List<Flight> getAllFlights(){
		FlightModel flightModel = getFlightModel();
		List<Flight> flights = flightModel.getFlights();
		return flights;
	}	
}
