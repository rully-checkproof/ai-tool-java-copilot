package com.checkproof.explore.ai_tool_java_copilot.config;

import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class NatsConfig {

  @Value("${nats.server.url:nats://127.0.0.1:4222}")
  private String natsServerUrl;

  @Bean
  public Connection natsConnection() throws IOException, InterruptedException {
    return Nats.connect(natsServerUrl);
  }
}
