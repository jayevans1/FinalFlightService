package com.cooksys.core.mvc;

import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.core.dao.service.UserService;
import com.cooksys.core.models.UserModel;
import com.cooksys.security.PasswordBcyrpt;
import com.hibernate.User;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	
	@Autowired
	PasswordBcyrpt passwordBcyrpt;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public @ResponseBody UserModel clientLogin(@FormParam("username")String username, @FormParam("password")String password){
		User user = userService.getUserByUsername(username);
		
		if(user != null){
			System.out.println("CHECKING PASSWORD : " + password);
			if(passwordBcyrpt.match(password, user.getPassword())){
				System.out.println("LOG IN SUCCES ");
				UserModel userModel = new UserModel();
				userModel.setFirstname(user.getFirstname());
				userModel.setLastname(user.getLastname());
				userModel.setUsername(user.getUsername());
				userModel.setEmail(user.getEmail());
				userModel.setStreet(user.getStreet());
				userModel.setCity(user.getCity());
				userModel.setState(user.getState());
				return userModel;
			}
		}
		return new UserModel();
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public @ResponseBody String clientSignup(@RequestBody User user){
		if(userService.getUserByUsername(user.getUsername()) == null){
			UserModel userModel = new UserModel();
			userModel.setUsername(user.getUsername());
			userModel.setPassword(passwordBcyrpt.hash(user.getPassword()));
			userModel.setFirstname(user.getFirstname());
			userModel.setLastname(user.getLastname());
			userModel.setUsername(user.getUsername());
			userModel.setEmail(user.getEmail());
			userModel.setStreet(user.getStreet());
			userModel.setCity(user.getCity());
			userModel.setState(user.getState());
			userService.createUser(user);
			return "success";
		}
		
		return "fail";
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody UserModel editPage(@RequestBody UserModel userModel){
		if(userService.getUserByUsername(userModel.getUsername()) != null){
			User user = userService.getUserByUsername(userModel.getUsername());
			user.setPassword(passwordBcyrpt.hash(user.getPassword()));
			user.setFirstname(userModel.getFirstname());
			user.setLastname(userModel.getLastname());
			user.setUsername(userModel.getUsername());
			user.setEmail(userModel.getEmail());
			user.setStreet(userModel.getStreet());
			user.setCity(userModel.getCity());
			user.setState(userModel.getState());
			userService.updateUser(user);
			user = userService.getUserByUsername(userModel.getUsername());
			userModel.setUsername(userModel.getUsername());
			userModel.setPassword(passwordBcyrpt.hash(userModel.getPassword()));
			userModel.setFirstname(userModel.getFirstname());
			userModel.setLastname(userModel.getLastname());
			userModel.setUsername(userModel.getUsername());
			userModel.setEmail(userModel.getEmail());
			userModel.setStreet(userModel.getStreet());
			userModel.setCity(userModel.getCity());
			userModel.setState(userModel.getState());
			return userModel;
		}
		return new UserModel();
	}
}
