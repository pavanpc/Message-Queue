package com.example.queue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.example.service.InFlightMessage;
import com.example.service.Message;
import com.example.service.QueueConfig;
import com.example.util.Util;


/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents an Abstraction for Queue implementation
 * It implements an interface IQueue where all the method signatures can be found
 * The default implementation can be written here for Pull and delete operations
 * The implementations for Push, Poll and isempty is left to classes which inherit this class.
 * ALso, it stores a data structure to handle inFlight Messages 
 * A thread is initiated in the constructor to account for visibility Timeout.
 * The thread runs every 5 seconds which is configurable
 */


public abstract class AbstractQueue implements IQueue {

	protected final QueueConfig queueConfig;
	private final Timer timer = new Timer();
	private final ConcurrentHashMap<String, InFlightMessage> inFlightMessages = new ConcurrentHashMap<String, InFlightMessage>();

	public AbstractQueue(QueueConfig queueConfig) {
		this.queueConfig = queueConfig;

		// call this piece of code to decide the status of inflight of messages
		// Here the time is set to 5 seconds.
		 timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	
	            	for(String key: inFlightMessages.keySet()){
	            		
	                    InFlightMessage msg = inFlightMessages.get(key);
	                    if (Util.isGreaterThanCurrentTime((msg.getTimestamp()))) {
	                    	// if current time greater than message pull time , push/put it at front of the queue
	                        push(msg.getMessage());
	                    }
	            	}
	            }
	        }, 5000/* 5 seconds */);
	}

	public QueueConfig getQueueConfig() {
		return queueConfig;
	}


	public synchronized Message pull() {
		// delete form queue and push it to Inflight messages store ie , Map
		// TODO - instead of using concurrentHash Map we can use concurrentSet in orde to make iteration faster.
		//        If data is huge, iteration over HashMap is a costly operation
		Message message = this.poll();
		// set message time to now+visibilityTimeout
		// here N= visibilityTimeout
		Date date = Util.getTimeAfterNSeconds(queueConfig.getVisibilityTimeout());
		inFlightMessages.put(message.getMessageId(), new InFlightMessage(message,
				new Timestamp(date.getTime())));
		System.out.println("Pulled Message --" + message.getBody());
		System.out.println(getAllInFlightMessages());
		return message;
	}

	public  boolean delete(String messageId) {
		return (inFlightMessages.remove(messageId) !=null);
	}

	// method to stop queue timer which checks for inflight messages
	public void stop() {
        timer.cancel();
    }

	/*
	 * Utitlity method to get Inflight messages data at any given point of time
	 */
	public ArrayList<Timestamp> getAllInFlightMessages() {
		ArrayList<Timestamp> messageIds= new ArrayList<Timestamp>();
		
		for(String key: inFlightMessages.keySet())
		{
			messageIds.add(inFlightMessages.get(key).getTimestamp());
		}
		return messageIds;
	}
	/*
	 *  poll from queue or a file..Leave it to subclass
	 */
	public abstract Message poll(); 
	
	/*
	 *  this is implementattion specific- can be a queue/file
	*/
	public abstract boolean push(Message message); 
	/*
	 * check if queue is empty. Leave implementation to child class
	 */
	public abstract boolean isEmpty();
}
