package com.example.service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.example.exceptions.QueueExistsException;
import com.example.queue.AbstractQueue;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * Stores all queues related information
 * It has a concurrent Hash map with Queuename as key and Abstract queue as value
 * Making value as Abstract has a good advantage of keeping any types of queues in this Map.

 */

public class QueueRegistry {

	private ConcurrentHashMap<String, AbstractQueue> queueRegister;

	public QueueRegistry() {
		queueRegister = new ConcurrentHashMap<String, AbstractQueue>();

	}

	public boolean addQueue(String queueName, AbstractQueue queue) {
		if (queueExists(queueName))
			throw new QueueExistsException("Queue " + queueName
					+ " Already Exists !!");
		else {
			queueRegister.put(queueName, queue);
			return true;
		}

	}

	public synchronized boolean removeQueue(String queueName) {

		AbstractQueue queue = this.getQueue(queueName);
		if (queue != null) {
			// this can be handled in abstract queue class also in finalize
			// method,
			// we need to wait till Garbage collection for this to happen..
			// Handling this explicitly is safe..
			queue.stop();
			
			queueRegister.remove(queueName);
			return true;
		} else
			return false;

	}

	// method to get queue given queue name
	public AbstractQueue getQueue(String queueName) {
		if (queueExists(queueName))
			return queueRegister.get(queueName);
		else
			throw new QueueDoesNotExistException("Queue " + queueName
					+ " Does not  Exist !!");

	}

	// method to check if a queue already exists
	public boolean queueExists(String queueName) {

		return (queueRegister.containsKey(queueName));
	}

	public void stop() {
		for (String key : queueRegister.keySet()) {
			AbstractQueue queue = queueRegister.get(key);
			queue.stop();
		}
	}

	public ArrayList<String> getAllQueueNames() {
		ArrayList<String> queueNames = new ArrayList<String>();

		for (String key : queueRegister.keySet()) {
			queueNames.add(key);
		}
		return queueNames;
	}

}
