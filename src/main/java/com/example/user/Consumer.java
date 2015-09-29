package com.example.user;

import com.example.service.AbstractQueueService;
import com.example.service.Message;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a consumer which can pull messages from a queue
 * It can pull messages from any queue based on queue Service  instance
 * 
 */
public class Consumer {

	private final AbstractQueueService queueService;
	private final String queueName;

	public Consumer(AbstractQueueService queueService, String queueName) {
		this.queueService = queueService;
		this.queueName = queueName;
	}

	public Message pull() {
		return queueService.pull(queueName);
	}

	public Boolean delete(String messageId) {
		return queueService.delete(queueName, messageId);
	}

	public String getQueueName() {
		return queueName;
	}
}