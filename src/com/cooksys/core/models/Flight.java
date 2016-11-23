package com.cooksys.core.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Flight implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final AtomicInteger NEXTID = new AtomicInteger(1);
	
	@XmlElement
	private Integer flightId;
	
	@XmlElement
	private Location origin;
	@XmlElement
	private Location destination;
	
	@XmlElement
	private Integer departure;
	@XmlElement
	private Integer eta;
	
//	@Override
//	public String toString() {
//		String flightDetails;
//		flightDetails = "Flight Id: " + flightId +"\n"+
//						"From: " + origin.getCity() + "," + origin.getState() + "\n" +
//						"To: " + destination.getCity() + "," + destination.getState() + "\n" + 
//						"Departure: " + departure + " days\n" +
//						"ETA: " + eta + " day flight\n";
//		return flightDetails;
//	}
	
	public String toString(){
		String flightDetails;
		flightDetails = "Flight Id: " + flightId + " From: " + origin.getCity() + "," + origin.getState() +
				" To: " + destination.getCity() + "," + destination.getState() + 
				" Departure: " + departure + " days " +
				"ETA: " + eta + " day flight";
		return flightDetails;
	}
	
//	public String toString(){
//		String id =Integer.toString(flightId) + " Origin : " + origin.getCity() +" Destination : " + destination.getCity();
//		return id;
//	}
	
	public static Integer getNextFlightID(){
		return NEXTID.incrementAndGet();
	}
	
	public Integer getFlightId() {
		return flightId;
	}
	public void setFlightId(Integer flightId) {
		this.flightId = flightId;
	}
	public Location getOrigin() {
		return origin;
	}
	public void setOrigin(Location origin) {
		this.origin = origin;
	}
	public Location getDestination() {
		return destination;
	}
	public void setDestination(Location destination) {
		this.destination = destination;
	}
	public Integer getDeparture() {
		return departure;
	}
	public void setDeparture(Integer departure) {
		this.departure = departure;
	}
	public Integer getEta() {
		return eta;
	}
	public void setEta(Integer eta) {
		this.eta = eta;
	}
}
