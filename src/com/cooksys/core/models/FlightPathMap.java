package com.cooksys.core.models;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightPathMap {
	
	@XmlElement
	Map<Integer, FlightList> map;

	public Map<Integer, FlightList> getMap() {
		return map;
	}

	public void setMap(Map<Integer, FlightList> map) {
		this.map = map;
	}
	
	
}
