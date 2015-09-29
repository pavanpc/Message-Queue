package com.example.queue;

import com.example.service.Message;
import com.example.service.QueueConfig;

/* Represents an SQSqueue class.This can be implemented by writing an adapter for SQS and 
 * getting corresponding queue instance here.
 * 
 */

public class SQSQueue extends AbstractQueue{

	public SQSQueue(QueueConfig queueConfig) {
		super(queueConfig);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean push(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
