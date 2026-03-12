package com.abhishek.student_api.exception;

// import com.abhishek.student_api.entity.Student;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id){
        super("Student not found with id:  " + id);
    }
    
}
