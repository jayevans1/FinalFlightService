package com.cooksys.core.mvc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightList;
import com.cooksys.core.tools.FlightUpdatesJMS;

@Controller
public class FlightUpdatesController {

	@Autowired
	FlightUpdatesJMS flightUpdates;
	
	@RequestMapping(value="/getFlightsDelayed", method=RequestMethod.GET)
	public @ResponseBody FlightList sendFlightsDelayed(){
		List<Flight> flightsDelay = flightUpdates.getFlightsDelay();
		
		FlightList flightList = new FlightList();
		flightList.setFlightList(flightsDelay);
		return flightList;
	}
	
	@RequestMapping(value="/getFlightsArrived", method=RequestMethod.GET)
	public @ResponseBody Flight sendFlightsArrived(){
		Flight arrivedFlight = flightUpdates.getFlightArrived();
		return arrivedFlight;
	}
}
