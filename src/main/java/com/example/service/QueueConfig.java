package com.example.service;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * Stores all configuration related to a queue 
 * It has queue name and Visibility Timeout vlaue in seconds
 * 
 */
public class QueueConfig {

	private String queueName;
	private int visibilityTimeoutSeconds = 2; // in seconds, default 2 seconds

	public int getVisibilityTimeout() {
		return visibilityTimeoutSeconds;
	}

	public void setVisibilityTimeout(int visibilityTimeout) {
		this.visibilityTimeoutSeconds = visibilityTimeout;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
