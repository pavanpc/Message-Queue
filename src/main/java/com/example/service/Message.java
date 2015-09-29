package com.example.service;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a Message
 * It has message body and Uniqueus message ID
 * Message Id is unique across all queue systems
 * 
 */
public class Message {

	private String messageId;
	private Object body;

	public Message(String data) {
		this.body = data;
	}

	public Message() {
		// TODO Auto-generated constructor stub
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
