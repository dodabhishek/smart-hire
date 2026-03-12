package com.smarthire.candidateservice.service;

import com.smarthire.candidateservice.event.CandidateRegisteredEvent;
import com.smarthire.candidateservice.model.Candidate;
import com.smarthire.candidateservice.repository.CandidateRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String TOPIC = "candidate.registered";

    public Candidate register(Candidate candidate) {
        // 1. Save to PostgreSQL
        Candidate saved = candidateRepository.save(candidate);
        log.info("Candidate saved: id={}", saved.getId());

        // 2. Publish event to Kafka
        CandidateRegisteredEvent event = CandidateRegisteredEvent.builder()
                .candidateId(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .currentTitle(saved.getCurrentTitle())
                .occurredAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, saved.getId().toString(), event);
        log.info("Published event to Kafka topic: {}", TOPIC);

        return saved;
    }
}