package com.checkproof.explore.ai_tool_java_copilot.repository;

import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE " +
        "(:status IS NULL OR t.status = :status) AND " +
        "(:startDate IS NULL OR t.startDate >= :startDate) AND " +
        "(:endDate IS NULL OR t.endDate <= :endDate) " +
        "ORDER BY t.createdAt DESC")
    Page<Task> findTasks(
        @Param("status") TaskStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable);

}