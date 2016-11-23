package com.cooksys.core.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import com.hibernate.User;

@Component
@Transactional
public class UserDAOImpl implements UserDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public User getUserById(int userId) {
		Query query = getCurrentSession().createQuery("From User where userId = :userId");
		query.setInteger("userId", userId);
		
		return (User)query.uniqueResult();
	}

	@Override
	public User getUserByUsername(String username) {
		Query query = getCurrentSession().createQuery("From User where username = :username");
		query.setString("username", username);
		User user = (User)query.uniqueResult();
		if(user != null){
			Hibernate.initialize(user.getUsername());
			Hibernate.initialize(user.getPassword());
			Hibernate.initialize(user.getUserFlights());
		}
		return user;
	}

	@Override
	public List<User> getAllUser() {
		List<User> users = getCurrentSession().createQuery("From User").list();
		users.forEach(u->{
			Hibernate.initialize(u.getUserFlights());
		});
		return users;
	}

	@Override
	public void createUser(User user) {
		getCurrentSession().save(user);
	}
	
	@Override
	public void updateUser(User user){
		getCurrentSession().update(user);
	}

	@Override
	public void deleteUser(User user) {
		getCurrentSession().delete(user);
	}
	
	private Session getCurrentSession()
	{
		return sessionFactory.getCurrentSession();
	}

}
