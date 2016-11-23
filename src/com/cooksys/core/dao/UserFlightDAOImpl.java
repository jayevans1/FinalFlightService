package com.cooksys.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hibernate.User;
import com.hibernate.UserFlight;

@Component
public class UserFlightDAOImpl implements UserFlightDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<UserFlight> getAllUserFlight() {
		Query query = getCurrentSession().createQuery("From UserFlight");

		List<UserFlight> userFlights = query.list();

		return userFlights;
	}

	@Override
	public List<UserFlight> getUserFlightByUser(User user) {
		Query query = getCurrentSession().createQuery("From UserFlight where user = :user order by userFlightId ASC");
		query.setParameter("user", user);
		
		List<UserFlight> userFlights = query.list();
		
		return userFlights;
	}
	
	@Override
	public void createUserFlight(UserFlight userFlight) {
		getCurrentSession().save(userFlight);
	}

	@Override
	public void deleteUserFlight(UserFlight userFlight) {
		getCurrentSession().delete(userFlight);
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
