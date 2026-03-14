package com.abhishek.student_api.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreatedEvent {
    private Long id;
    private String name;
    private String email;
    private String course;
}