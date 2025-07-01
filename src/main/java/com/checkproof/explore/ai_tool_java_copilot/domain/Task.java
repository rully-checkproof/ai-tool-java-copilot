package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskPriority;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private TaskPriority priority;
  private TaskStatus status;
  private RecurrenceType recurrenceType;
  private RecurrencePattern recurrencePattern;
  private List<Participant> participants;

}
