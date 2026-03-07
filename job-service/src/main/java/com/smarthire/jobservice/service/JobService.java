package com.smarthire.jobservice.service;

import com.smarthire.jobservice.event.JobPostedEvent;
import com.smarthire.jobservice.model.Job;
import com.smarthire.jobservice.repository.JobRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService {

    private final JobRepository jobRepository;

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public static final String TOPIC = "job.posted";

    public Job createJob(Job job) {
        Job saved = jobRepository.save(job);
        log.info("Job saved: id={}, title={}", saved.getId(), saved.getTitle());

        if (kafkaTemplate != null) {
            JobPostedEvent event = JobPostedEvent.builder()
                    .jobId(saved.getId())
                    .title(saved.getTitle())
                    .company(saved.getCompany())
                    .description(saved.getDescription())
                    .requiredSkills(saved.getRequiredSkills())
                    .location(saved.getLocation())
                    .occurredAt(LocalDateTime.now())
                    .build();
            try {
                kafkaTemplate.send(TOPIC, saved.getId().toString(), event).get(3, TimeUnit.SECONDS);
                log.info("Published event to Kafka topic: {}", TOPIC);
            } catch (Exception e) {
                log.warn("Kafka unavailable, event not published: {}", e.getMessage());
            }
        }

        return saved;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getActiveJobs() {
        return jobRepository.findByStatus("ACTIVE");
    }
}