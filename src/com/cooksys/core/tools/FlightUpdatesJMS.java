package com.cooksys.core.tools;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;

@Component
public class FlightUpdatesJMS {

	private static final String JMS_URL = "tcp://localhost:61616";
	private static final String TOPIC_NAME = "FlightUpdate";

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;
	MessageConsumer consumer;
	
	private boolean connected;
	
	private List<Flight> flightsDelay;
	private Flight flightArrived;
	
	Logger log = LoggerFactory.getLogger(FlightUpdatesJMS.class);	
	
	@PostConstruct
	public void getFlightStatus(){
		
		try {
			connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
			destination = session.createTopic(TOPIC_NAME);
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					if (message instanceof ObjectMessage) {
						ObjectMessage obj = (ObjectMessage) message;
						try {
							String flightStatus = obj.getStringProperty("FlightStatus");
							
							if(flightStatus.equals("Flights Delayed")){
								flightsDelay = (List<Flight>) obj.getObject();
								log.info("Delayed Flights : ");
								flightsDelay.forEach(f->{
									
									log.info(Integer.toString(f.getFlightId()));
								});
							}
							
							if(flightStatus.equals("Flight Arrived")){
								flightArrived = (Flight) obj.getObject();
								log.info(flightArrived.getFlightId() + " has arrived");
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("hi in catch");
							e.printStackTrace();
						}
					}
				}
			});
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void shutdown(){
		this.connected = false;
		try{
			this.connection.stop();
			this.connection.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public List<Flight> getFlightsDelay() {
		return flightsDelay;
	}

	public void setFlightsDelay(List<Flight> flightsDelay) {
		this.flightsDelay = flightsDelay;
	}

	public Flight getFlightArrived() {
		return flightArrived;
	}

	public void setFlightArrived(Flight flightArrived) {
		this.flightArrived = flightArrived;
	}

}
