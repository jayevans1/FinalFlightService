package com.cooksys.core.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@Component
public class FlightModelDAOImpl implements FlightModelDAO {

	@Override
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

	@Override
	public List<Flight> getAllFlights() {
		FlightModel flightModel = getFlightModel();

		List<Flight> flights = flightModel.getFlights();

		return flights;
	}

}
