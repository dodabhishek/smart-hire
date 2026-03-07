package com.smarthire.candidateservice.controller;

import com.smarthire.candidateservice.model.Candidate;
import com.smarthire.candidateservice.repository.CandidateRepository;
import com.smarthire.candidateservice.service.CandidateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController              // ← Handles HTTP requests, returns JSON automatically
@RequestMapping("/api/candidates")
@RequiredArgsConstructor     // ← Lombok injects CandidateRepository via constructor
public class CandidateController {

    private final CandidateRepository candidateRepository;
    private final CandidateService candidateService;  // ← Add this

    @GetMapping
    public List<Candidate> getAll() {
        return candidateRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Candidate> create(@RequestBody Candidate candidate) {
        // Now calls the service which saves + publishes Kafka event
        return ResponseEntity.ok(candidateService.register(candidate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getById(@PathVariable Long id) {
        return candidateRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}






