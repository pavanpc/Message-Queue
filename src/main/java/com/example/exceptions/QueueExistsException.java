package com.example.exceptions;
public class QueueExistsException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public QueueExistsException(String msg) {
        super(msg);
    }
}
