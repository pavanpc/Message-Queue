package com.example.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/*
 * A Class to read configuration files like local.properties, prod.properties
 * By default it reads from root directory of the project.the path can be taken as an argument
 * 
 */

public class ConfigReader {

	Properties prop = new Properties();
	FileInputStream fis;
	File file;
	public ConfigReader(String path)
	{
		try {
			file= new File(path);
			fis = new FileInputStream(file);
			
			prop.load(fis);
			//file.
			//ile.close();	
			
			System.out.println(file.getName());
		} catch (Exception e) {
		}
	}
	
	/* Here Onlly local.properties file is checked
	 We can read environment specific properties file like prod.properties and return 
	correspnding instance
	
	Also we can read setVivisibility from property file if required as a configuration
	*/
	public AbstractQueueService getServiceInstance()
	{
		AbstractQueueService queueService=null;
		if(file.toString().equalsIgnoreCase("local.properties"))
		{
			// if its in memory queue return inmemory queue service 
			if(prop.getProperty("impl").equalsIgnoreCase("InmemoryQueue"))
				queueService= new InMemoryQueueService();
			else if(prop.getProperty("impl").equalsIgnoreCase("FileQueue"))
				queueService= new FileQueueService();
		}
		
		return queueService;
	}
	
	
}