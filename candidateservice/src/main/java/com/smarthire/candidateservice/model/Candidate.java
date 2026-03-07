package com.smarthire.candidateservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity                  // ← Tells JPA: "this class = a database table"
@Table(name = "candidates")
@Getter @Setter          // ← Lombok generates getters/setters for you
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String currentTitle;
    private Integer yearsOfExperience;

    // We'll add skills and resume later
}