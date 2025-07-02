package com.checkproof.explore.ai_tool_java_copilot.util;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.dto.ParticipantDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.RecurrencePatternDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TaskEventMapper {
    public static Task toTaskEntity(TaskEventMessageDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setRecurrenceType(dto.getRecurrenceType());
        return task;
    }

    public static RecurrencePattern toRecurrencePatternEntity(RecurrencePatternDto dto) {
        RecurrencePattern rp = new RecurrencePattern();
        rp.setId(dto.getId());
        rp.setType(dto.getType());
        rp.setRecurrenceInterval(dto.getInterval());
        rp.setDaysOfWeek(dto.getDaysOfWeek());
        rp.setDayOfMonth(dto.getDayOfMonth());
        return rp;
    }

    public static Participant toParticipantEntity(ParticipantDto dto) {
        Participant p = new Participant();
        p.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        p.setName(dto.getName());
        p.setTitle(dto.getTitle());
        p.setRole(dto.getRole());
        return p;
    }

    public static void updateTaskEntity(Task existingTask, TaskEventMessageDto dto) {
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStartDate(dto.getStartDate());
        existingTask.setEndDate(dto.getEndDate());
        existingTask.setPriority(dto.getPriority());
        existingTask.setStatus(dto.getStatus());
        existingTask.setRecurrenceType(dto.getRecurrenceType());

        // Update RecurrencePattern in-place
        updateRecurrencePattern(existingTask, dto.getRecurrencePattern());

        // Update Participants in-place
        updateParticipants(existingTask, dto.getParticipants());
    }

    private static void updateRecurrencePattern(Task task, RecurrencePatternDto dto) {
        if (dto != null) {
            if (task.getRecurrencePattern() == null) {
                task.setRecurrencePattern(toRecurrencePatternEntity(dto));
            } else {
                RecurrencePattern rp = task.getRecurrencePattern();
                rp.setType(dto.getType());
                rp.setRecurrenceInterval(dto.getInterval());
                rp.setDaysOfWeek(dto.getDaysOfWeek());
                rp.setDayOfMonth(dto.getDayOfMonth());
            }
        } else {
            task.setRecurrencePattern(null);
        }
    }

    private static void updateParticipants(Task task, List<ParticipantDto> participantDtos) {
        if (participantDtos != null) {
            if (task.getParticipants() == null) {
                task.setParticipants(new ArrayList<>());
            }
            task.getParticipants().clear();
            participantDtos.forEach(participantDto -> task.getParticipants().add(toParticipantEntity(participantDto)));
        } else {
            if (task.getParticipants() != null) {
                task.getParticipants().clear();
            }
        }
    }
}
