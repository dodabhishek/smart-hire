package com.abhishek.student_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Student Request controls what the user is Allowed to sent They cannot send an id for examples that db generates
public class StudentRequest {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message ="Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

     @NotBlank(message = "Course cannot be empty")
    private String course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    

}
