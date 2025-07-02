package com.checkproof.explore.ai_tool_java_copilot.service;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskService {

  void handleTaskEvent(TaskEventMessageDto taskEventMessageDto);

  Page<TaskDto> getAllTasks(Pageable pageable, TaskStatus status, LocalDateTime startDate, LocalDateTime endDate);

  TaskDto getTaskById(UUID id);
}
