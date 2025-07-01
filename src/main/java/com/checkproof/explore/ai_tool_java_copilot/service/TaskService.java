package com.checkproof.explore.ai_tool_java_copilot.service;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;

public interface TaskService {

  void handleTaskEvent(TaskEventMessageDto taskEventMessageDto);

}
