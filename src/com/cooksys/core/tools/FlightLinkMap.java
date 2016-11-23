package com.cooksys.core.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;

@Component
public class FlightLinkMap {
	//map that stores Flights as key, and the keys value are its possible next flights
    private Map<Flight, LinkedHashSet<Flight>> map = new HashMap();

    /**
     * Links The Flight
     * @param node1
     * @param node2
     */
    public void addEdge(Flight node1, Flight node2) {
        LinkedHashSet<Flight> adjacent = map.get(node1);
       
        	if(adjacent == null){
        		adjacent = new LinkedHashSet();
        		map.put(node1, adjacent);
        	}
        	adjacent.add(node2);
        
    }

    public Map<Flight, LinkedHashSet<Flight>> getMap(){
    	return map;
    }

    /**
     * Returns List of Flights Linked to the parameter Flight
     * 
     * @param last
     * @return
     */
    public LinkedList<Flight> adjacentNodes(Flight last) {
        LinkedHashSet<Flight> adjacent = map.get(last);
        if(adjacent==null) {
            return new LinkedList();
        }
        return new LinkedList<Flight>(adjacent);
    }
}
