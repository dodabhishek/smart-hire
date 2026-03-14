package com.abhishek.notification_service.listener;

import com.abhishek.notification_service.event.StudentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @KafkaListener(
        topics = "student.created",
        groupId = "notification-group"
    )
    public void handleStudentCreated(StudentCreatedEvent event) {
        log.info("📬 Notification received for new student!");
        log.info("👤 Name: {}", event.getName());
        log.info("📧 Email: {}", event.getEmail());
        log.info("📚 Course: {}", event.getCourse());
        log.info("✅ Welcome email would be sent to: {}", event.getEmail());
    }
}