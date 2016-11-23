package com.cooksys.core.dao.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cooksys.core.dao.UserDAO;
import com.hibernate.User;

@Component
@Transactional
public class UserService {

	@Autowired
	UserDAO userDAO;
	
	/**
	 * get User by Id
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId){
		return userDAO.getUserById(userId);
	}
	
	/**
	 * get User by username
	 * @param username
	 * @return
	 */
	public User getUserByUsername(String username){
		return userDAO.getUserByUsername(username);
	}
	
	/**
	 * create new User
	 * @param user
	 */
	public void createUser(User user){
		userDAO.createUser(user);
	}
	
	/**
	 * Updates User
	 * @param user
	 */
	public void updateUser(User user){
		userDAO.updateUser(user);
	}
}
