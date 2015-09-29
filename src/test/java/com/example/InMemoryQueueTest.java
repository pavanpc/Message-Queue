package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.service.AbstractQueueService;
import com.example.service.InMemoryQueueService;
import com.example.service.Message;
import com.example.service.QueueConfig;
import com.example.user.Consumer;
import com.example.user.Producer;


/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * Test class to test Inmemory queue behavior like
 * 1. create queue
 * 2. push messages
 * 3 .pull messages
 * 4 .Check visibility timeout behavior
 * 		case a: delete is not called before timeout period, message appears in queue
 * 		case b: delete is called before timeout, message should get deleted
 * 
 */
public class InMemoryQueueTest {

	@Test
	public void testFileQueueCreation() {
		AbstractQueueService inMemorQueueService = new InMemoryQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("InMemoryTestQueue");
		boolean returnStatus = inMemorQueueService.createQueue(config);
		assertEquals(true, returnStatus);

	}

	@Test
	public void testPushSingleMessage() {
		AbstractQueueService inMemorQueueService = new InMemoryQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("InMemoryTestQueue");
		inMemorQueueService.createQueue(config);
		Producer pub = new Producer(inMemorQueueService, "InMemoryTestQueue");
		assertEquals(true, pub.push("One"));

	}

	@Test
	public void testPullOnce() {
		AbstractQueueService inMemorQueueService = new InMemoryQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("InMemoryTestQueue");
		inMemorQueueService.createQueue(config);
		String queueName = "InMemoryTestQueue";
		Producer pub = new Producer(inMemorQueueService, queueName);
		assertEquals(true, pub.push("One"));
		assertEquals(true, pub.push("Two"));
		assertEquals(true, pub.push("Three"));
		Consumer consumer = new Consumer(inMemorQueueService, queueName);

		assertEquals("One", consumer.pull().getBody());
		assertEquals("Two", consumer.pull().getBody());
		assertEquals("Three", consumer.pull().getBody());

	}

	/*
	 * Case 1; check for - message appearing again in the queue after timeout
	 * Case 2: check for - message should not be there if delete is called
	 * before timeout
	 */
	@Test
	public void testVisibilityTimeOut() {

		AbstractQueueService inMemorQueueService = new InMemoryQueueService();
		QueueConfig config = new QueueConfig();
		config.setQueueName("InMemoryTestQueue");
		// set visibility timeout to 2 seconds
		config.setVisibilityTimeout(2);
		inMemorQueueService.createQueue(config);
		String queueName = "InMemoryTestQueue";
		Producer pub = new Producer(inMemorQueueService, queueName);
		assertEquals(true, pub.push("One"));
		Consumer consumer = new Consumer(inMemorQueueService, queueName);
		assertEquals("One", consumer.pull().getBody());
		assertEquals(true, inMemorQueueService.isEmpty(queueName));
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
		assertEquals(true, inMemorQueueService.isEmpty(queueName));

	}

/*
 *    Below methods can be used to check mulitple producers and consumers
 */
	// public static void main(String[] args) {
	// AbstractQueueService queueService = new InMemoryQueueService();
	//
	// try {
	//
	// String queue1 = "simple1";
	// String queue2 = "simple2";
	// QueueConfig queueConfig = new QueueConfig();
	// queueConfig.setQueueName(queue1);
	// queueConfig.setVisibilityTimeout(300);
	// queueService.createQueue(queueConfig);
	//
	// queueConfig.setQueueName(queue2);
	// queueService.createQueue(queueConfig);
	//
	// final Producer pub = new Producer(queueService, queue1);
	// final Consumer consumer = new Consumer(queueService, queue1);
	//
	// final Producer pub1 = new Producer(queueService, queue2);
	// final Consumer consumer1 = new Consumer(queueService, queue2);
	//
	// final AtomicLong value = new AtomicLong(0L);
	// Thread publisherThreads = new Thread() {
	// public void run() {
	// pub.push("Some data 1 : " + value.incrementAndGet());
	// System.out.println("Publish data 1 : " + value.get());
	// }
	// };
	//
	// Thread consumerThreads = new Thread() {
	// public void run() {
	// Message msg = consumer.peek();
	// System.out.println("Consumer 1 : " + msg.getBody());
	// Boolean value = consumer.delete(msg.getMessageId());
	// System.out.println("Consumer 1 : " + value);
	//
	// }
	// };
	//
	// Thread publisherThreads1 = new Thread() {
	// public void run() {
	// pub1.push("Some data 2 : " + value.incrementAndGet());
	// System.out.println("Publish data  2 : " + value.get());
	// }
	// };
	//
	// Thread consumerThreads1 = new Thread() {
	// public void run() {
	// Message msg = consumer1.peek();
	// System.out.println("Consumer 2 : " + msg.getBody());
	// Boolean value = consumer1.delete(msg.getMessageId());
	// System.out.println("Consumer 2 : " + value);
	// }
	// };
	//
	// ExecutorService executor = Executors.newFixedThreadPool(5);
	//
	// for (int i = 0; i < 10; ++i) {
	// executor.execute(publisherThreads);
	// executor.execute(publisherThreads1);
	// }
	// for (int i = 0; i < 10; ++i) {
	// executor.execute(consumerThreads);
	// executor.execute(consumerThreads1);
	// if (i % 2 == 0) {
	// executor.execute(publisherThreads);
	// executor.execute(publisherThreads1);
	// }
	// }
	//
	// executor.shutdown();
	//
	// while (!executor.isTerminated()) {
	// }
	//
	// System.out.println("Finished all threads");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// queueService.stop();
	// }
	//
	// }
}
