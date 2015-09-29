package com.example.service;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 *  An interface to represent all operations supported by queue service as mentioned below
 *  1. Push(queueName,Message)
 *  2. Pull(queueName)
 *  3. delete(queueName,MessageId)
 *  4. createQueue(queueName)
 *  5. deleteQueue(queueName)
 */

public interface QueueService {

	public boolean push(String queueName, Message message);

	/*
	 * Pull message from a specific queue
	 */
	public Message pull(String queueName);

	/*
	 * Delete message given message Id from a specific queue
	 */
	public boolean delete(String queueName, String messageId);

	/*
	 * Create a new queue given queueName
	 */

	public boolean createQueue(QueueConfig config);

	/*
	 * Delete a new queue given queueName
	 */

	public boolean deleteQueue(String queueName);

}
