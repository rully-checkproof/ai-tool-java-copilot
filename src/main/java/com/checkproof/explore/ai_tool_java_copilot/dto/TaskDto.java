package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskPriority;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task data transfer object containing all task information")
public class TaskDto {

    @Schema(description = "Unique identifier for the task", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Task title/name", example = "Complete project documentation", maxLength = 255)
    private String title;

    @Schema(description = "Detailed description of the task", example = "Write comprehensive documentation for the new API endpoints")
    private String description;

    @Schema(description = "Task start date and time", example = "2025-07-07T09:00:00")
    private LocalDateTime startDate;

    @Schema(description = "Task end date and time", example = "2025-07-07T17:00:00")
    private LocalDateTime endDate;

    @Schema(description = "Priority level of the task", example = "HIGH")
    private TaskPriority priority;

    @Schema(description = "Current status of the task", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Type of recurrence pattern", example = "WEEKLY")
    private RecurrenceType recurrenceType;

    @Schema(description = "Recurrence pattern details if task is recurring")
    private RecurrencePatternDto recurrencePattern;

    @Schema(description = "List of participants assigned to this task")
    private List<ParticipantDto> participants;
}
