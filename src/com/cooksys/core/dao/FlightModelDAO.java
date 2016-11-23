package com.cooksys.core.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
@Component
public interface FlightModelDAO {

	public FlightModel getFlightModel();
	
	public List<Flight> getAllFlights();
}
