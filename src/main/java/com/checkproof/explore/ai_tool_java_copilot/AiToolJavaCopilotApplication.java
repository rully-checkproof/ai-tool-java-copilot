package com.checkproof.explore.ai_tool_java_copilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AiToolJavaCopilotApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiToolJavaCopilotApplication.class, args);
	}

}
