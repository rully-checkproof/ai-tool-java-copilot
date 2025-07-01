package com.checkproof.explore.ai_tool_java_copilot.service.impl;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

  @Override
  public void handleTaskEvent(TaskEventMessageDto taskEventMessageDto) {
    log.info("Received task event: {}", taskEventMessageDto);
  }
}
