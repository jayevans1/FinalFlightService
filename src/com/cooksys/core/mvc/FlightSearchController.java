package com.cooksys.core.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.core.dao.service.FlightModelService;
import com.cooksys.core.dao.service.FlightSearch;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightList;
import com.cooksys.core.models.FlightModel;
import com.cooksys.core.models.Location;
import com.cooksys.core.models.LocationList;
import com.hibernate.User;

@Controller
public class FlightSearchController {

	@Autowired
	FlightModelService flightModelService;
	
	@Autowired
	FlightSearch flightSearch;
	
	Logger log = LoggerFactory.getLogger(FlightSearchController.class);
	
	/**
	 * Send all Flights
	 * @return
	 */
	@RequestMapping(value="/getAllFlight", method=RequestMethod.GET)
	public @ResponseBody FlightList sendAllFlights(){
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightModelService.getAllFlights());
		return flightList;
	}
	
	@RequestMapping(value="/getFlightPath", method=RequestMethod.POST)
	public @ResponseBody FlightList sendFlightPath(@FormParam("flightId") String flightId, @FormParam("destination") String destination){
		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
		log.info(originFlight.toString());
		flightSearch.search(originFlight, destination);
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightSearch.getFlightPath());
		return flightList;	
	}
	
	@RequestMapping(value="/getAllFlightOrigin", method=RequestMethod.GET)
	public @ResponseBody LocationList sendFlightOrigin(){
		Map<Location, List<Flight>> origin = flightModelService.getAllFlightOriginLocations();
		List<Location> originList = new ArrayList<Location>();
		origin.keySet().forEach(o->{
			originList.add(o);
			//log.info(o.getCity() + "," + o.getState());
		});
		
		LocationList locationList = new LocationList();
		locationList.setLocationList(originList);
		
		return locationList;
	}
	
	@RequestMapping(value="/getAllFlightDestination", method=RequestMethod.GET)
	public @ResponseBody LocationList sendFlightDestination(){
		Map<Location, List<Flight>> destination = flightModelService.getAllFlightDestinationLocations();
		List<Location> destinationList = new ArrayList<Location>();
		destination.keySet().forEach(d->{
			destinationList.add(d);
			//log.info(d.getCity() + "," + d.getState());
		});
		
		LocationList locationList = new LocationList();
		locationList.setLocationList(destinationList);
		
		return locationList;
	}
	
	@RequestMapping(value="/getAllFlightFromOrigin", method=RequestMethod.POST)
	public @ResponseBody FlightList sendAllFlightFromOrigin(@RequestBody Location origin){
		Map<Location, List<Flight>> flightFromOriginMap = flightModelService.getAllFlightOriginLocations();
		List<Flight> flightsFromOrigin = new ArrayList<Flight>();
	
		flightFromOriginMap.get(origin).forEach(f->{
			flightsFromOrigin.add(f);
		});
		
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightsFromOrigin);
		
		return flightList;
	}
	
	@RequestMapping(value="/path", method=RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/xml")
	public @ResponseBody FlightList path(@FormParam("flightId") String flightId, @FormParam("destination")String destination){
		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
		log.info(originFlight.toString());
		List<Flight> nextFlights = flightSearch.path(originFlight, destination);
		
		FlightList flightList = new FlightList();
		flightList.setFlightList(nextFlights);
		
		return flightList;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public @ResponseBody FlightList search(@FormParam("flightId") String flightId, @FormParam("destination") String destination){
		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
		log.info(originFlight.toString());
		flightSearch.search(originFlight, destination);
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightSearch.getFlightPath());
		return flightList;	
	}
	
//	@RequestMapping(value="/test", method=RequestMethod.POST)
//	public @ResponseBody FlightList test(@FormParam("flightId") String flightId, @FormParam("destination") String destination){
//		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
//		log.info(originFlight);
//		Map<Flight,List<Flight>> path = flightModelService.getFlightRoutes(originFlight, destination);
//		Set<Flight> set = path.keySet();
//		List<Flight> next = new ArrayList<Flight>();
//		set.forEach(f->{
//			next.add(f);
//		});
//		
//		FlightList flightList = new FlightList();
//		flightList.setFlightList(next);
//		
//		return flightList;
//	}
	
//	@RequestMapping(value="/test", method=RequestMethod.POST)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces("application/xml")
//	public @ResponseBody FlightList test(@FormParam("flightId") String flightId){
//		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
//		log.info(originFlight);
//		List<Flight> nextFlights = flightSearch.getFlightNextFlights(originFlight);
//		
//		FlightList flightList = new FlightList();
//		flightList.setFlightList(nextFlights);
//		
//		return flightList;
//	}
	
//	@RequestMapping(value="/test", method=RequestMethod.GET)
//	public @ResponseBody FlightList test(){
//		Flight originFlight = flightModelService.getFlightByFlightId(725);
//		log.info(originFlight);
//		List<Flight> nextFlights = flightSearch.getFlightNextFlights(originFlight);
//		
//		FlightList flightList = new FlightList();
//		flightList.setFlightList(nextFlights);
//		
//		return flightList;
//	}
	

	
//	@RequestMapping(value="/test", method=RequestMethod.POST)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces("application/xml")
//	public @ResponseBody FlightList test(@FormParam("flightId") String flightId){
//		Flight originFlight = flightModelService.getFlightByFlightId(Integer.parseInt(flightId));
//		log.info(originFlight);
//		List<Flight> nextFlights = flightSearch.test(originFlight, "Atlanta");
//		
//		FlightList flightList = new FlightList();
//		flightList.setFlightList(nextFlights);
//		
//		return flightList;
//	}
}
