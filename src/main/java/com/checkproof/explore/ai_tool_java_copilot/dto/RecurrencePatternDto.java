package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurrencePatternDto {
  private UUID id;
  private RecurrenceType type; // DAILY, WEEKLY, MONTHLY
  private Integer interval; // e.g., every 2 days
  private List<String> daysOfWeek; // e.g., "MONDAY,WEDNESDAY"
  private Integer dayOfMonth; // e.g., 15 for monthly
  private LocalDate endDate;
}

