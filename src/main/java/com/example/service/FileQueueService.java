package com.example.service;

import com.example.queue.AbstractQueue;
import com.example.queue.FileQueue;
/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a FileQueue Service   which extends AbstractQueueService
 * 
 */

public class FileQueueService extends AbstractQueueService {

	/*
	 * Returns a File queue 
	 *  */
	@Override
	protected AbstractQueue getNewQueue(QueueConfig config) {
		FileQueue fileQueue= new FileQueue(config);
		return fileQueue;
	}
  
 
}

