package com.smarthire.candidateservice.repository;

import com.smarthire.candidateservice.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // You get save(), findById(), findAll(), deleteById() for FREE
    // Spring generates the actual SQL — you just call methods
}