package com.checkproof.explore.ai_tool_java_copilot.controller;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Get all tasks with pagination and optional filtering
     * @param pageable Pagination parameters (page, size, sort)
     * @param status Optional filter by task status
     * @param startDate Optional filter by start date (tasks starting after this date)
     * @param endDate Optional filter by end date (tasks ending before this date)
     * @return Paginated list of tasks
     */
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            Pageable pageable,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Getting tasks with filters - status: {}, startDate: {}, endDate: {}, page: {}",
                status, startDate, endDate, pageable);

        Page<TaskDto> tasks = taskService.getAllTasks(pageable, status, startDate, endDate);

        log.info("Found {} tasks out of {} total", tasks.getNumberOfElements(), tasks.getTotalElements());

        return ResponseEntity.ok(tasks);
    }

    /**
     * Get a specific task by ID
     * @param id Task ID
     * @return Task details
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable UUID id) {
        log.info("Getting task by ID: {}", id);

        TaskDto task = taskService.getTaskById(id);

        return ResponseEntity.ok(task);
    }
}
