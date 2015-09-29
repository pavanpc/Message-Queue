package com.example;

import com.example.service.AbstractQueueService;
import com.example.service.ConfigReader;

public class Main {

	public static void main(String args[]) throws Exception {
		String prpoertFileName;
		if (args.length == 1) {
			prpoertFileName = args[0];
		} else {
			System.out.println("Please pass Environment property File !!");
			return;
		}

		/*
		 * Property file can have
		 * impl=FileQueue/InmemoryQueue/SsqQueue
		 * 
		 */
		ConfigReader configReader = new ConfigReader(prpoertFileName);
		// Get appropriate Queue service implementation
		AbstractQueueService queueService = configReader.getServiceInstance();

	}

}
