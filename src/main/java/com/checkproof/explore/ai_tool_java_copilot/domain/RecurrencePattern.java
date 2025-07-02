package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurrence_patterns")
public class RecurrencePattern {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RecurrenceType type; // DAILY, WEEKLY, MONTHLY

    @Column(name = "recurrence_interval")
    private Integer recurrenceInterval; // e.g., every 2 days

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recurrence_days_of_week", joinColumns = @JoinColumn(name = "recurrence_pattern_id"))
    @Column(name = "day_of_week")
    private List<String> daysOfWeek; // e.g., MONDAY, WEDNESDAY

    private Integer dayOfMonth; // e.g., 15 for monthly

    private LocalDate endDate;
}
