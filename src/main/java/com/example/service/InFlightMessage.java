package com.example.service;

import java.sql.Timestamp;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents an Inflight Message
 * It has message object and timestamp to accounf for visibility timeout
 * 
 */
public class InFlightMessage {

	private final Message message;
	private Timestamp timestamp;

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public InFlightMessage(Message message, java.sql.Timestamp timestamp) {
		this.message = message;

		this.timestamp = timestamp;
	}

	public Message getMessage() {
		return message;
	}

}
