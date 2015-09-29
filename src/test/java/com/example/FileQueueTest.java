package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.service.AbstractQueueService;
import com.example.service.FileQueueService;
import com.example.service.Message;
import com.example.service.QueueConfig;
import com.example.user.Consumer;
import com.example.user.Producer;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * Test class to test File queue behavior like
 * 1. create queue
 * 2. push messages
 * 3 .pull messages
 * 4 .Check visibility timeout behavior
 * 		case a: delete is not called before timeout period, message appears in queue
 * 		case b: delete is called before timeout, message should get deleted
 * 
 */
public class FileQueueTest {
	@Test
	public void testFileQueueCreation() {
		AbstractQueueService fileQueueService = new FileQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("FileTestQueue");
		boolean returnStatus = fileQueueService.createQueue(config);
		System.out.println(fileQueueService.getAllQueueNames());
		assertEquals(true, returnStatus);
		assertEquals(true,fileQueueService.deleteQueue("FileTestQueue"));

	}

	@Test
	public void testPushSingleMessage() {
		AbstractQueueService fileQueueService = new FileQueueService();
		System.out.println(fileQueueService.getAllQueueNames());
		QueueConfig config = new QueueConfig();
		config.setQueueName("FileTestQueue");
		fileQueueService.createQueue(config);
		Message msg = new Message();
		msg.setBody("One");
		boolean returnStatus = fileQueueService.push("FileTestQueue", msg);
		assertEquals(true, returnStatus);
		assertEquals(true,fileQueueService.deleteQueue("FileTestQueue"));

	}

	@Test
	public void testPullMessages() {
		AbstractQueueService fileQueueService = new FileQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("FilePullTestQueue");
		fileQueueService.createQueue(config);
		String queueName = "FilePullTestQueue";
		Producer pub = new Producer(fileQueueService, queueName);
		assertEquals(true, pub.push("One"));
		assertEquals(true, pub.push("Two"));
		assertEquals(true, pub.push("Three"));
		Consumer consumer = new Consumer(fileQueueService, queueName);

		assertEquals("One", consumer.pull().getBody());
		assertEquals("Two", consumer.pull().getBody());
		assertEquals("Three", consumer.pull().getBody());
		assertEquals(true,fileQueueService.deleteQueue(queueName));

	}

	/*
	 * Case 1; check for - message appearing again in the queue after timeout
	 * Case 2: check for - message should not be there if delete is called
	 * before timeout
	 */
	@Test
	public void testVisibilityTimeOut() {

		AbstractQueueService fileQueueService = new FileQueueService();
		System.out.println(fileQueueService.getAllQueueNames());
		QueueConfig config = new QueueConfig();
		config.setQueueName("FileVisibilityTestQueue");
		// set visibility timeout to 2 seconds
		config.setVisibilityTimeout(2);
		fileQueueService.createQueue(config);
		String queueName = "FileVisibilityTestQueue";
		Producer pub = new Producer(fileQueueService, queueName);
		assertEquals(true, pub.push("One"));
		Consumer consumer = new Consumer(fileQueueService, queueName);
		assertEquals("One", consumer.pull().getBody());
		assertEquals(true, fileQueueService.isEmpty(queueName));
		// sleep for one second
		try {
			// sleep for 6 seconds , as thread to push messages runs every 5
			// seconds
			Thread.sleep(6000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		// message should appear again in queue since delete is not called
		Message pulledMessage = consumer.pull();
		assertEquals("One", pulledMessage.getBody());

		// sleep for one second
		try {
			// sleep for 4 seconds , as thread to push messages runs every 5
			// seconds
			Thread.sleep(4000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		// Now the message should not re appear as it is called before timeout
		assertEquals(true, consumer.delete(pulledMessage.getMessageId()));

		try {
			// sleep for 2 seconds , as thread to push messages runs every 5
			// seconds
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		// the queue should be empty after 6 seconds of pull .
		assertEquals(true, fileQueueService.isEmpty(queueName));
		assertEquals(true,fileQueueService.deleteQueue(queueName));


	}

}
