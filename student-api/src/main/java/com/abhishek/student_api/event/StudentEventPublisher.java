package com.abhishek.student_api.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentEventPublisher {

    private final KafkaTemplate<String, StudentCreatedEvent> kafkaTemplate;

    public static final String STUDENT_CREATED_TOPIC = "student.created";

    public void publishStudentCreated(StudentCreatedEvent event) {
        kafkaTemplate.send(STUDENT_CREATED_TOPIC, event);
        System.out.println("📤 Event published: " + event);
    }
}