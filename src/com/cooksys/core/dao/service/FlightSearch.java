package com.cooksys.core.dao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.RequestScoped;
import javax.print.attribute.HashAttributeSet;

import org.apache.commons.net.finger.FingerClient;
import org.hibernate.dialect.H2Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cooksys.core.dao.FlightModelDAO;
import com.cooksys.core.models.Flight;
import com.cooksys.core.tools.FlightLinkMap;

@Component
@RequestScoped
public class FlightSearch {

	@Autowired
	FlightModelDAO flightModelDAO;
	
	List<Flight> flightPath = new ArrayList<Flight>();

	Logger log = LoggerFactory.getLogger(FlightSearch.class);

	/**
	 * Gets selected Flight next possible Path Flights
	 * 
	 * @param originFlight
	 * @return
	 */
	public List<Flight> getFlightNextFlights(Flight originFlight) {
		List<Flight> allFlights = flightModelDAO.getAllFlights();

		allFlights.remove(originFlight);

		List<Flight> nextPossibleFlights = new ArrayList<Flight>();

		allFlights.forEach(f -> {
			if (f.getOrigin().getCity()
					.equals(originFlight.getDestination().getCity())) {
				nextPossibleFlights.add(f);
			}
		});

		List<Flight> tempList = new ArrayList<Flight>();

		nextPossibleFlights.forEach(f -> {
			if (((originFlight.getDeparture() + originFlight.getEta()) > f
					.getDeparture())) {
				//log.info("Removing : " + f + "\n" + originFlight);
			} else {
				tempList.add(f);
			}
		});

		//log.info(originFlight.toString());
		tempList.forEach(f -> {
			//log.info(f.toString());
		});

		return tempList;
	}

	/**
	 * Recursive method to create a flightLinkMap of possible flights from a
	 * origin Flight till it reaches its destination Flight
	 * @param flightLinkMap
	 * @param originFlight
	 * @param destination
	 */
	public void test(FlightLinkMap flightLinkMap, Flight originFlight, String destination) {

		List<Flight> nextFlight = getFlightNextFlights(originFlight);
		
		if (!originFlight.getDestination().getCity().equals(destination)) {
			//log.info(originFlight.toString() + " possible flights");
			nextFlight.forEach(f -> {
				flightLinkMap.addEdge(originFlight, f);
				//log.info(f.toString());
			});
			
			nextFlight.forEach(f -> {
				test(flightLinkMap,f,destination);
			});
		}else{
			flightLinkMap.addEdge(originFlight, originFlight);
		}

	}
	
	/**
	 * Call recursive method to make the flight path flightLinkMap
	 * then returns all the possible flight paths from origin to destination
	 * @param originFlight
	 * @param destination
	 * @return
	 */
	public List<Flight> path(Flight originFlight, String destination){
		Map<Flight, LinkedHashSet<Flight>> map = new HashMap<Flight, LinkedHashSet<Flight>>();
		FlightLinkMap flightLinkMap = new FlightLinkMap();
		
		test(flightLinkMap, originFlight, destination);
		
		map=flightLinkMap.getMap();
		
		Set<Flight> set = map.keySet();
		List<Flight> allPossiblePathFlights = new ArrayList<Flight>();
		
		for(Flight f : set){
			log.info(f + " :: All Possible Paths");
			allPossiblePathFlights.add(f);
			for(Flight ff : map.get(f)){
				log.info(ff.toString());
			}
			log.info("\n");
		}
		return allPossiblePathFlights;
	}
	
	/**
	 * 
	 * @param flightLinkMap
	 * @param destination
	 * @return
	 */
	public Flight flightToFinalDestination(FlightLinkMap flightLinkMap, String destination){
		Flight flightDestination = null;
		log.info("THE DESTINATION IS  " + destination);
		for(Flight f : flightLinkMap.getMap().keySet()){
			for(Flight ff : flightLinkMap.getMap().get(f)){
				if(ff.getDestination().getCity().equals(destination)){
					flightDestination = ff;
				}
			}
		}
		log.info("++++++++++++++++++++++");
		log.info("DESTINATION FLIGHT IS " + flightDestination);
		log.info("++++++++++++++++++++++");
		return flightDestination;
	}
	
	/**
	 * Search for flight paths starting at Origin Flight to 
	 * a Destination Flight
	 * @param originFlight
	 * @param destination
	 */
	public void search(Flight originFlight, String destination){
		
		FlightLinkMap flightLinkMap = new FlightLinkMap();
		
		test(flightLinkMap, originFlight, destination);
		
		Flight flightToDestination= flightToFinalDestination(flightLinkMap, destination);
		
		LinkedList<Flight> visited =  new LinkedList<Flight>();
		
		visited.add(originFlight);
		
		log.info("PATHS================= from : " + flightToDestination);
		
		if(originFlight.equals(flightToDestination)){
			flightPath.clear();
			flightPath.add(originFlight);
		}else{
			flightPath.clear();
			findShortestFlightPath(flightLinkMap, visited, flightToDestination);
		}
	}
	
	/**
	 * 
	 * @param flightLinkMap
	 * @param visited
	 * @param flightToDestination
	 */
	private void findShortestFlightPath(FlightLinkMap flightLinkMap, LinkedList<Flight> visited, Flight flightToDestination) {
		LinkedList<Flight> nodes = flightLinkMap.adjacentNodes(visited.getLast());
		// examine adjacent nodes
		for (Flight node : nodes) {
			if (visited.contains(node)) {
				continue;
			}
			if (node.equals(flightToDestination)) {
				visited.add(node);
				printPath(visited);
				visited.removeLast();
				break;
			}
		}
		// in breadth-first, recursion needs to come after visiting adjacent
		// nodes
		for (Flight node : nodes) {
			if (visited.contains(node) || node.equals(flightToDestination)) {
				continue;
			}
			visited.addLast(node);
			findShortestFlightPath(flightLinkMap, visited, flightToDestination);
			visited.removeLast();
		}
	}

	/**
	 * Prints the flight paths
	 * @param visited
	 */
	private void printPath(LinkedList<Flight> visited) {
		flightPath.clear();
		for (Flight node : visited) {
			log.info(node.toString());
			log.info(" ");
			if(flightPath==null){
				flightPath = visited;
			}
			if(!flightPath.contains(node)){
				flightPath.add(node);
			}
			
		}
		log.info("\n");
		
	}
	
	/**
	 * Get Flight by Flight Id
	 * @param flightId
	 * @return
	 */
	public Flight getFlightByFlightId(int flightId){
		List<Flight>  flights = flightModelDAO.getAllFlights();
		
		 Flight selectedFlight = null;
		
		 for(Flight f : flights){
			 if(f.getFlightId() == flightId){
					selectedFlight = f;
					break;
				}
		 }
		 return selectedFlight;
	}

	public List<Flight> getFlightPath() {
		return flightPath;
	}

	public void setFlightPath(List<Flight> flightPath) {
		this.flightPath = flightPath;
	}
}
