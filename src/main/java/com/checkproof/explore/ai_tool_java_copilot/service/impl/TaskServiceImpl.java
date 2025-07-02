package com.checkproof.explore.ai_tool_java_copilot.service.impl;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.dto.ParticipantDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.RecurrencePatternDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import com.checkproof.explore.ai_tool_java_copilot.repository.ParticipantRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.RecurrencePatternRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.TaskRepository;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final RecurrencePatternRepository recurrencePatternRepository;
  private final ParticipantRepository participantRepository;

  @Override
  @Transactional
  public void handleTaskEvent(TaskEventMessageDto dto) {
    log.info("Received task event: {}", dto);
    if (dto == null || dto.getId() == null) {
      log.warn("Received null or invalid task event DTO: {}", dto);
      return;
    }
    RecurrencePattern recurrencePattern = null;
    if (dto.getRecurrencePattern() != null) {
      var recurrencePatternDto = dto.getRecurrencePattern();
      // Try to find existing pattern if ID is provided
      if (recurrencePatternDto.getId() != null) {
        recurrencePattern = recurrencePatternRepository.findById(recurrencePatternDto.getId())
            .map(existingPattern -> {
              log.info("Pattern found for ID: {}", recurrencePatternDto.getId());
              return existingPattern;
            })
            .orElseGet(() -> {
              log.warn("Pattern not found for ID: {}, creating new one", recurrencePatternDto.getId());
              return createAndSaveRecurrencePattern(recurrencePatternDto);
            });
      } else {
        // Create new RecurrencePattern without ID (let it be generated)
        recurrencePattern = createAndSaveRecurrencePattern(recurrencePatternDto);
      }
    }

    // Handle participants
    List<Participant> participants = new ArrayList<>();
    if (dto.getParticipants() != null) {
      for (var participantDto : dto.getParticipants()) {
        if (participantDto.getId() != null) {
          // Check if participant already exists
          Optional<Participant> existingParticipant = participantRepository.findById(
              participantDto.getId());
          if (existingParticipant.isPresent()) {
            log.info("Participant found for ID: {}", participantDto.getId());
            participants.add(existingParticipant.get());
          } else {
            log.warn("Participant not found for ID: {}, creating new one", participantDto.getId());
            // If not found, create a new participant but don't save it manually
            // Let cascade handle the saving
            Participant participant = TaskEventMapper.toParticipantEntity(participantDto);
            participants.add(participant);
          }
        }
      }
    }

    Task task = taskRepository.findById(dto.getId()).orElse(null);
    if (task == null) {
      // Create new Task from DTO
      log.info("Creating new Task for ID: {}", dto.getId());
      task = TaskEventMapper.toTaskEntity(dto);
    } else {
      // Update existing Task using mapper
      log.info("Updating existing Task for ID: {}", dto.getId());
      TaskEventMapper.updateTaskEntity(task, dto);
    }
    if (recurrencePattern != null) {
      task.setRecurrencePattern(recurrencePattern);
    }
    // Set participants for the task
    if (!participants.isEmpty()) {
      task.setParticipants(participants);
    }
    log.debug("saving task: {}", task);
    taskRepository.save(task);
    log.info("Task event processed successfully: {}", dto);
  }

  private RecurrencePattern createAndSaveRecurrencePattern(RecurrencePatternDto recurrencePatternDto) {
    RecurrencePattern recurrencePattern = TaskEventMapper.toRecurrencePatternEntity(recurrencePatternDto);
    return recurrencePatternRepository.saveAndFlush(recurrencePattern);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TaskDto> getAllTasks(Pageable pageable, TaskStatus status, LocalDateTime startDate, LocalDateTime endDate) {
    log.info("Fetching tasks with filters - status: {}, startDate: {}, endDate: {}, taskId: {}",
             status, startDate, endDate);

    Page<Task> taskPage = taskRepository.findTasksWithFilters(pageable, status, startDate, endDate);

    return taskPage.map(this::convertToTaskDto);
  }

  @Override
  @Transactional(readOnly = true)
  public TaskDto getTaskById(UUID id) {
    log.info("Fetching task by ID: {}", id);

    Task task = taskRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

    return convertToTaskDto(task);
  }

  private TaskDto convertToTaskDto(Task task) {
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
            convertToRecurrencePatternDto(task.getRecurrencePattern()) : null)
        .participants(task.getParticipants() != null ?
            task.getParticipants().stream()
                .map(this::convertToParticipantDto)
                .toList() : List.of())
        .build();
  }

  private RecurrencePatternDto convertToRecurrencePatternDto(RecurrencePattern pattern) {
    return RecurrencePatternDto.builder()
        .id(pattern.getId())
        .type(pattern.getType())
        .interval(pattern.getRecurrenceInterval())
        .daysOfWeek(pattern.getDaysOfWeek())
        .dayOfMonth(pattern.getDayOfMonth())
        .endDate(pattern.getEndDate())
        .build();
  }

  private ParticipantDto convertToParticipantDto(Participant participant) {
    return ParticipantDto.builder()
        .id(participant.getId())
        .name(participant.getName())
        .title(participant.getTitle())
        .department(participant.getDepartment())
        .role(participant.getRole())
        .build();
  }
}
