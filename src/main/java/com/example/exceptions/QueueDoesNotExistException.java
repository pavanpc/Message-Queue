package com.example.exceptions;

public class QueueDoesNotExistException extends RuntimeException{

	public QueueDoesNotExistException(String msg)
	{
		
		super(msg);
	}
}
