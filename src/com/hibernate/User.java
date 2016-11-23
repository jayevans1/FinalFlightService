package com.hibernate;

// Generated Jun 1, 2015 9:36:47 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "flightservice", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements java.io.Serializable {

	@XmlElement
	private Integer userId;
	@XmlElement
	private String username;
	@XmlElement
	private String password;
	@XmlElement
	private String firstname;
	@XmlElement
	private String lastname;
	@XmlElement
	private String email;
	@XmlElement
	private String street;
	@XmlElement
	private String city;
	@XmlElement
	private String state;
	@XmlElement
	private Set<UserFlight> userFlights = new HashSet<UserFlight>(0);

	public User() {
	}

	public User(String username, String password, String firstname,
			String lastname, String email, String street, String city,
			String state) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.street = street;
		this.city = city;
		this.state = state;
	}

	public User(String username, String password, String firstname,
			String lastname, String email, String street, String city,
			String state, Set<UserFlight> userFlights) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.street = street;
		this.city = city;
		this.state = state;
		this.userFlights = userFlights;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "username", unique = true, nullable = false, length = 45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 60)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "firstname", nullable = false, length = 45)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "lastname", nullable = false, length = 45)
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "email", nullable = false, length = 45)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "street", nullable = false, length = 45)
	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "city", nullable = false, length = 45)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state", nullable = false, length = 45)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserFlight> getUserFlights() {
		return this.userFlights;
	}

	public void setUserFlights(Set<UserFlight> userFlights) {
		this.userFlights = userFlights;
	}

}
