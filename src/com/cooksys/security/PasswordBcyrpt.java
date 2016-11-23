package com.cooksys.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordBcyrpt {

	Logger log = LoggerFactory.getLogger(PasswordBcyrpt.class);
	/**
	 * Bcrypt Password
	 * @param password
	 * @return
	 */
	public String hash(String password){
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		String hashPass = b.encode(password);
		return hashPass;
	}
	
	/**
	 * Checks to see if raw password matches hashed password
	 * @param pass
	 * @param hash
	 * @return
	 */
	public boolean match(String pass, String hash){
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		if(b.matches(pass, hash)){
			log.info("PASS word MATHCES");
			return true;
		}else{
			log.info("PASS word Does Not MATHCE");
			return false;
		}
	}

	
}
