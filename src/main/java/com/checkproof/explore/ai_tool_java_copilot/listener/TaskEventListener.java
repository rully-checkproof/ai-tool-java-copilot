package com.checkproof.explore.ai_tool_java_copilot.listener;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventListener {

  private final TaskService taskService;
  private final ObjectMapper objectMapper;
  private final Connection natsConnection;

  @Value("${nats.subject:tasks}")
  private String natsSubject;

  @PostConstruct
  public void subscribeToNats() {
    try {
      Dispatcher dispatcher = natsConnection.createDispatcher(msg -> {
        log.info("Received message on subject {}: {}", natsSubject, new String(msg.getData()));
        try {
          final TaskEventMessageDto event = objectMapper.readValue(msg.getData(), TaskEventMessageDto.class);
          taskService.handleTaskEvent(event);
        } catch (IOException e) {
          log.error("Failed to deserialize message: {}", new String(msg.getData()), e);
        }
      });
      dispatcher.subscribe(natsSubject);
    } catch (Exception e) {
      log.error("Failed to subscribe to NATS subject: {}", natsSubject, e);
    }
  }

  @PreDestroy
  public void closeNatsConnection() {
    if (natsConnection != null) {
      try {
        natsConnection.close();
        log.info("NATS connection closed.");
      } catch (Exception e) {
        log.error("Error closing NATS connection", e);
      }
    }
  }

}