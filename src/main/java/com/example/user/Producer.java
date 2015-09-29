package com.example.user;

import com.example.service.Message;
import com.example.service.AbstractQueueService;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * The class represents a producer which can push messages to a queue
 * It can push messages to any queue based on queue Service  instance
 * 
 */
public class Producer {

    private final AbstractQueueService queueService;
    private final String queueName;

    public Producer(AbstractQueueService queueService, String queueName) {
        this.queueService = queueService;
        this.queueName = queueName;
    }
    
    public Boolean push(String data) {
        return queueService.push(queueName, new Message(data));
    }
    
    public String getQueueName() {
        return queueName;
    }
}
