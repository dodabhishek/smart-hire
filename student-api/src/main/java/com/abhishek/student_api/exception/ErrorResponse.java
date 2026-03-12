package com.abhishek.student_api.exception;

import java.time.LocalDateTime;


public class ErrorResponse {
    private int status;
    private String message ;
    private LocalDateTime timeStamp;
    public ErrorResponse(int notFound, String message) {
        this.status = notFound;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }
    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    
    
}
