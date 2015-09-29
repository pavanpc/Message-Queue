package com.example.queue;

import com.example.service.Message;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 *  An interface to represent all operations on Queue
 */
public interface IQueue {

	public boolean push(Message message);

	public Message pull();

	public boolean delete(String queueName);

}
