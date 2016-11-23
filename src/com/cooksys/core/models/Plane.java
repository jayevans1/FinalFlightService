package com.cooksys.core.models;

import java.util.ArrayDeque;
import java.util.Deque;

public class Plane {
	
	private Location currentLocation;
	private Location nextLocation;
	
	private Deque<Flight> flightDeque = new ArrayDeque<>();
	
	public Plane(){
		super();
	}
	public Plane(Location currentLocation, Location nextLocation) {
		super();
		this.currentLocation = currentLocation;
		this.nextLocation = nextLocation;
	}
	
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	public Location getNextLocation() {
		return nextLocation;
	}
	public void setNextLocation(Location nextLocation) {
		this.nextLocation = nextLocation;
	}
	public Deque<Flight> getFlightDeque() {
		return flightDeque;
	}
	public void setFlightDeque(Deque<Flight> flightDeque) {
		this.flightDeque = flightDeque;
	}
	
}
