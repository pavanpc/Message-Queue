package com.example.service;

import java.util.ArrayList;
import java.util.UUID;

import com.example.exceptions.QueueCreationException;
import com.example.queue.AbstractQueue;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents an Abstraction for QueueService implementation
 * It implements an interface QueueSerice where all the method signatures can be found
 * The default implementation can be written here for Push, Pull, delete, deletequeue 
 * CreateQueue implementation is left to classes which inherit this
 * ALso, it Queue Registry object to maintain all queues 
 * Operation are synchronized to make it Thread safe
 */

public abstract class AbstractQueueService implements QueueService {
	QueueRegistry queueRegistry = new QueueRegistry();

	public synchronized boolean push(String queueName, Message message) {

		AbstractQueue abstractQueue = queueRegistry.getQueue(queueName);
		message.setMessageId(UUID.randomUUID().toString());
		// not push is implemented in base classes Inmemory Queue & File
		// queue.Corresponsing push method will be called
		// for File and in memory queue
		return abstractQueue.push(message);
	}

	public synchronized Message pull(String queueName) {
		AbstractQueue abstractQueue = queueRegistry.getQueue(queueName);
		return abstractQueue.pull();
	}

	public synchronized boolean delete(String queueName, String messageId) {

		AbstractQueue queue = queueRegistry.getQueue(queueName);
		return queue.delete(messageId);
	}

	public synchronized boolean createQueue(QueueConfig config) {

		AbstractQueue queue = getNewQueue(config);
		if (queue == null)
			throw new QueueCreationException("Error creating Queue- "
					+ config.getQueueName());
		return queueRegistry.addQueue(config.getQueueName(), queue);

	}

	// this will be implemented by child class Inmemory Queue/ File Queue
	protected abstract AbstractQueue getNewQueue(QueueConfig config);

	public synchronized boolean deleteQueue(String queueName) {
		
		return queueRegistry.removeQueue(queueName);

	}

	public ArrayList<String> getAllQueueNames() {
		return queueRegistry.getAllQueueNames();
	}

	public void stop() {
		queueRegistry.stop();
	}

	public boolean isEmpty(String queueName) {
		AbstractQueue queue = queueRegistry.getQueue(queueName);
		return queue.isEmpty();
	}
}
