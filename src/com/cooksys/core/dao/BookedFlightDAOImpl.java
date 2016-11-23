package com.cooksys.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hibernate.BookedFlight;

@Component
public class BookedFlightDAOImpl implements BookedFlightDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<BookedFlight> getAllBookedFlight() {
		Query query = getCurrentSession().createQuery("From BookedFlight");
		
		List<BookedFlight> bookedFlights = query.list();
		
		return bookedFlights;
	}
	
	@Override
	public BookedFlight getFlightByFlightId(int flightId) {
		Query query = getCurrentSession().createQuery("From BookedFlight where flightId = :flightId");
		query.setInteger("flightId", flightId);
		
		BookedFlight bookedFlight = (BookedFlight)query.uniqueResult();
		
		return bookedFlight;
	}

	@Override
	public void createBookedFlight(BookedFlight bookedFlight) {
		getCurrentSession().save(bookedFlight);
	}

	@Override
	public void deleteBookedFlight(BookedFlight bookedFlight) {
		getCurrentSession().delete(bookedFlight);
	}
	
	private Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void updateBookedFlight(BookedFlight bookedFlight) {
		getCurrentSession().update(bookedFlight);
	}


}
