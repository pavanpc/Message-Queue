package com.example.queue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.example.service.Message;
import com.example.service.QueueConfig;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a File queue which extends Abstract Queue 
 * Here to store messages, a File is used .
 * By default it will be stored in sqs/ folder of your project.The path can be read and configured
 * A file represents a queue. 
 * Every access on file is made synchronized to make it Thread safe. 
 * We can also use locks to make it atomic.
 * 
 */

public class FileQueue extends AbstractQueue {
	private final File queue;
	Writer output;
	String filePath;

	public FileQueue(QueueConfig config) {
		super(config);
		filePath = "sqs/";
		this.queue = new File(filePath + config.getQueueName());
		try {
			this.queue.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public synchronized boolean push(Message message) {

		try {

			output = new BufferedWriter(new FileWriter(filePath
					+ queueConfig.getQueueName(), true));
			String content = (String) message.getBody();
			String messageId = message.getMessageId();
			String combinedMessage = messageId + "$" + content
					+ System.lineSeparator();
			;
			output.append(combinedMessage);
			output.close();
			return true;

		} catch (IOException e) {

		}
		return false;

	}

	/*
	 * @see com.example.queue.AbstractQueue#poll() For polling a message do the
	 * following Read first line from a file Put rest in a temp file Construct a
	 * Message object from first line using MessagId and Content (using
	 * delimiter $) Rename temp file to Queue Name Return newly constructed
	 * message which will be stored as In flight message and removed later based
	 * on visibility timeout
	 */

	@Override
	public synchronized Message poll() {

		File tempFile = new File(filePath + "temp");
		File inputFile = new File(filePath + queueConfig.getQueueName());

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String firstLine = reader.readLine();
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				writer.write(currentLine + System.lineSeparator());
			}
			writer.close();
			reader.close();
			boolean successful = tempFile.renameTo(inputFile);
			Message message = new Message();
			String[] firstLineSplitonDelimeter = firstLine.split("\\$");
			String messageId = firstLineSplitonDelimeter[0];
			// Remove new line at the end if exists
			String messageBody = firstLineSplitonDelimeter[1].trim();
			message.setBody(messageBody);
			message.setMessageId(messageId);

			return message;

		} catch (IOException e) {
		}
		return null;
	}
	
	public boolean delete(){
		return queue.delete();
	}

	public boolean isEmpty() {
		// we need to explicitly delete the file, instead of handing it over to GC
		return (queue.length() == 0);
	}
}
