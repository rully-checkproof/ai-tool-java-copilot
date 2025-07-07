package com.checkproof.explore.ai_tool_java_copilot.util;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.dto.ParticipantDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.RecurrencePatternDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TaskDtoMapper {

    public static TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .priority(task.getPriority())
                .status(task.getStatus())
                .recurrenceType(task.getRecurrenceType())
                .recurrencePattern(task.getRecurrencePattern() != null ?
                        toRecurrencePatternDto(task.getRecurrencePattern()) : null)
                .participants(task.getParticipants() != null ?
                        task.getParticipants().stream()
                                .map(TaskDtoMapper::toParticipantDto)
                                .toList() : List.of())
                .build();
    }

    public static RecurrencePatternDto toRecurrencePatternDto(RecurrencePattern pattern) {
        return RecurrencePatternDto.builder()
                .id(pattern.getId())
                .type(pattern.getType())
                .interval(pattern.getRecurrenceInterval())
                .daysOfWeek(pattern.getDaysOfWeek())
                .dayOfMonth(pattern.getDayOfMonth())
                .endDate(pattern.getEndDate())
                .build();
    }

    public static ParticipantDto toParticipantDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getId())
                .name(participant.getName())
                .title(participant.getTitle())
                .department(participant.getDepartment())
                .role(participant.getRole())
                .build();
    }
}
