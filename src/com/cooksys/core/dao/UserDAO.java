package com.cooksys.core.dao;

import java.util.List;

import com.hibernate.User;

public interface UserDAO {
	public User getUserById(int userId);
	public User getUserByUsername(String username);
	public List<User> getAllUser();
	public void createUser(User user);
	public void updateUser(User user);
	public void deleteUser(User user);
}
