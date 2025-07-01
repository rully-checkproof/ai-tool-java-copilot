package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskPriority;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;

import java.time.LocalDateTime;
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
public class TaskEventMessageDto {
  private UUID id;
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