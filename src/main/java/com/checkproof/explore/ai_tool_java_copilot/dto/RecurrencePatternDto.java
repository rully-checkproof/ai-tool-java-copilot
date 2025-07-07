package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Recurrence pattern configuration for recurring tasks")
public class RecurrencePatternDto {

  @Schema(description = "Unique identifier for the recurrence pattern", example = "789e0123-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Type of recurrence", example = "WEEKLY")
  private RecurrenceType type; // DAILY, WEEKLY, MONTHLY

  @Schema(description = "Interval between recurrences", example = "2", minimum = "1")
  private Integer interval; // e.g., every 2 days

  @Schema(description = "Days of the week for weekly recurrence", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
  private List<String> daysOfWeek; // e.g., "MONDAY,WEDNESDAY"

  @Schema(description = "Day of the month for monthly recurrence", example = "15", minimum = "1", maximum = "31")
  private Integer dayOfMonth; // e.g., 15 for monthly

  @Schema(description = "End date for the recurrence pattern", example = "2025-12-31")
  private LocalDate endDate;
}
