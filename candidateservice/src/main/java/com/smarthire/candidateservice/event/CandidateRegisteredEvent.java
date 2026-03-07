package com.smarthire.candidateservice.event;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRegisteredEvent {

    private Long candidateId;
    private String firstName;
    private String lastName;
    private String email;
    private String currentTitle;
    private LocalDateTime occurredAt;
}