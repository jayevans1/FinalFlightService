package com.cooksys.core.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.core.dao.service.BookedFlightService;
import com.cooksys.core.dao.service.UserFlightService;
import com.cooksys.core.dao.service.UserService;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightList;
import com.cooksys.core.models.FlightPathMap;
import com.cooksys.core.models.UserModel;
import com.hibernate.User;

@Controller
public class BookFlightController {
	
	@Autowired
	BookedFlightService bookedFlightService;
	
	@Autowired
	UserFlightService userFlightService;
	
	@Autowired
	UserService userService;
	
	Logger log = LoggerFactory.getLogger(BookFlightController.class);

	@RequestMapping(value="/bookFlight", method=RequestMethod.POST)
	public @ResponseBody String sendFlightPath(@RequestBody FlightList flightList){
		String username = flightList.getUsername();
		List<Flight> flights = flightList.getFlightList();
		bookedFlightService.doBookedFlight(username, flights, flights.get(0).getOrigin().getCity(), flights.get(flights.size()-1).getDestination().getCity());
		
		return "Booked";
	}
	
	@RequestMapping(value="/bookFlightPath", method=RequestMethod.POST)
	public @ResponseBody FlightList sendFlightPath(@RequestBody UserModel userModel){
		log.info("YOLO");
		User user = userService.getUserByUsername(userModel.getUsername());
//		Map<Integer, List<Flight>> map = userFlightService.getBookedFlightsByUser(user);
//		Map<Integer, FlightList> mapPath = new HashMap<Integer, FlightList>();
//		map.keySet().forEach(f->{
//			mapPath.put(f, new FlightList());
//			mapPath.get(f).setFlightList(map.get(f));
//		});
//		
//		FlightPathMap fpm = new FlightPathMap();
//		fpm.setMap(mapPath);
		List<Flight> flightPath = userFlightService.getBookedFlightPath(user);
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightPath);
		log.info("SENDING MAP");
		return flightList;
	}
}
