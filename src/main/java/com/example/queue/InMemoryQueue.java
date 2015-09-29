package com.example.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.exceptions.QueueEmptyExcpetion;
import com.example.service.Message;
import com.example.service.QueueConfig;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a Inmemory queue which extends Abstract Queue 
 * Here to store messages, a ConcurrentLinkedQueue 
 * We can also use Blocking queue, but this will block the entire queue where producers will not be able to producer while consumers read messages.There is a trade off
 * All operations which lead to contention are made synchronized which is thread safe
 * 
 */
public class InMemoryQueue extends AbstractQueue {
	private final ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();

	public InMemoryQueue(QueueConfig config) {
		super(config);

	}

	@Override
	public synchronized boolean push(Message message) {

		queue.add(message);
		System.out.println("Pushed message --" + message.getBody());
		return true;
	}

	@Override
	public synchronized Message poll() {
		if (queue.isEmpty())
			throw new QueueEmptyExcpetion("Queue -"
					+ queueConfig.getQueueName() + " is Empty !!");
		return queue.poll();

	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
	public boolean delete(){
		// here object is deleted when GC is called
		return true;
	}


}
