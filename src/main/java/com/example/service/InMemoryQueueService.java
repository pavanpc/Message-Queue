package com.example.service;

import com.example.queue.AbstractQueue;
import com.example.queue.InMemoryQueue;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents an Inmemory Queue Service   which extends AbstractQueueService
 * 
 */

public class InMemoryQueueService extends AbstractQueueService {

	@Override
	protected AbstractQueue getNewQueue(QueueConfig config) {
		InMemoryQueue inMemoryQueue = new InMemoryQueue(config);
		return inMemoryQueue;
	}

}
