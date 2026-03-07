package com.smarthire.jobservice.service;

import com.smarthire.jobservice.event.JobPostedEvent;
import com.smarthire.jobservice.model.Job;
import com.smarthire.jobservice.repository.JobRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC = "job.posted";

    public Job createJob(Job job) {
        // 1. Save to PostgreSQL
        Job saved = jobRepository.save(job);
        log.info("Job saved: id={}, title={}", saved.getId(), saved.getTitle());

        // 2. Publish event to Kafka
        JobPostedEvent event = JobPostedEvent.builder()
                .jobId(saved.getId())
                .title(saved.getTitle())
                .company(saved.getCompany())
                .description(saved.getDescription())
                .requiredSkills(saved.getRequiredSkills())
                .location(saved.getLocation())
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, saved.getId().toString(), event);
        log.info("Published event to Kafka topic: {}", TOPIC);

        return saved;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getActiveJobs() {
        return jobRepository.findByStatus("ACTIVE");
    }
}