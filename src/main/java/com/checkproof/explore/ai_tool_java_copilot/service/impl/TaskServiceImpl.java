package com.checkproof.explore.ai_tool_java_copilot.service.impl;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.repository.ParticipantRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.RecurrencePatternRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.TaskRepository;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    Optional<RecurrencePattern> existingPattern = recurrencePatternRepository.findById(dto.getRecurrencePattern().getId());
    if (existingPattern.isPresent()){
      log.warn("Pattern found for ID: {}", dto.getRecurrencePattern().getId());
    } else {
      // Create new RecurrencePattern from DTO
      recurrencePattern = TaskEventMapper.toRecurrencePatternEntity(dto.getRecurrencePattern());
      recurrencePatternRepository.saveAndFlush(recurrencePattern);
    }

    // Handle participants
    List<Participant> participants = new ArrayList<>();
    if (dto.getParticipants() != null) {
      for (var participantDto : dto.getParticipants()) {
        if (participantDto.getId() != null) {
          // Check if participant already exists
          Optional<Participant> existingParticipant = participantRepository.findById(participantDto.getId());
          if (existingParticipant.isPresent()) {
            log.info("Participant found for ID: {}", participantDto.getId());
            participants.add(existingParticipant.get());
          } else {
            log.warn("Participant not found for ID: {}", participantDto.getId());
            // If not found, create a new participant
            Participant participant = TaskEventMapper.toParticipantEntity(participantDto);
            participantRepository.saveAndFlush(participant);
            participants.add(participant);
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
    if (!participants.isEmpty()){
      task.setParticipants(participants);
    }
    log.debug("saving task: {}", task);
    taskRepository.save(task);
  }
    log.info("Task event processed successfully: {}", dto);
  }


}

