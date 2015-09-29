package com.example.exceptions;

public class QueueEmptyExcpetion extends RuntimeException{
	
	public QueueEmptyExcpetion(String msg)
	{
		super(msg);
	}
}
