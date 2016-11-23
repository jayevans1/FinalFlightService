package com.cooksys.core.dao;

import java.util.List;

import com.hibernate.User;
import com.hibernate.UserFlight;

public interface UserFlightDAO {
	
	public List<UserFlight> getAllUserFlight();
	public List<UserFlight> getUserFlightByUser(User user);
	public void createUserFlight(UserFlight userFlight);
	public void deleteUserFlight(UserFlight userFlight);
	
}
