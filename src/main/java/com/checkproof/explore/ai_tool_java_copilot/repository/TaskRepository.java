package com.checkproof.explore.ai_tool_java_copilot.repository;

import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
