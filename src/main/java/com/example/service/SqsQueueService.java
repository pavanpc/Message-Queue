package com.example.service;

import com.example.queue.AbstractQueue;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents SQS queue Service which extends AbstractQueueService
 * The methods are not implemented
 * Here we can create an instance of sqs queue using sqs client and return
 * 
 */
public class SqsQueueService extends  AbstractQueueService {

	@Override
	protected AbstractQueue getNewQueue(QueueConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
