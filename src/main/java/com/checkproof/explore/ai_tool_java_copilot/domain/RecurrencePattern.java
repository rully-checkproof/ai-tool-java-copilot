package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecurrencePattern {
  private RecurrenceType type; // DAILY, WEEKLY, MONTHLY
  private Integer interval; // e.g., every 2 days
  private List<String> daysOfWeek; // e.g., "MONDAY,WEDNESDAY"
  private Integer dayOfMonth; // e.g., 15 for monthly

}
