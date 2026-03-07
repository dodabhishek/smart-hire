package com.smarthire.jobservice.event;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostedEvent {
    private Long jobId;
    private String title;
    private String company;
    private String description;
    private List<String> requiredSkills;
    private String location;
    private LocalDateTime occurredAt;
}