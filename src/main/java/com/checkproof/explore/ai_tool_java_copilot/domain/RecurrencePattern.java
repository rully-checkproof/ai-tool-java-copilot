package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurrence_patterns")
public class RecurrencePattern {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RecurrenceType type; // DAILY, WEEKLY, MONTHLY

    @Column(name = "recurrence_interval")
    private Integer recurrenceInterval; // e.g., every 2 days

    @Column(name = "days_of_week", columnDefinition = "text[]")
    private List<String> daysOfWeek; // e.g., MONDAY, WEDNESDAY, FRIDAY

    private Integer dayOfMonth; // e.g., 15 for monthly

    private LocalDate endDate;
}
