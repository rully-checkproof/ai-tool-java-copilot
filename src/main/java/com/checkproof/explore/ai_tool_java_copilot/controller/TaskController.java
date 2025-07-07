package com.checkproof.explore.ai_tool_java_copilot.controller;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import com.checkproof.explore.ai_tool_java_copilot.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final TaskService taskService;

    @Operation(
        summary = "Get all tasks with filtering and pagination",
        description = """
            Retrieve a paginated list of tasks with optional filtering capabilities.
            
            **Filtering Options:**
            - **Status**: Filter by task status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
            - **Date Range**: Filter by start date and/or end date
            
            **Pagination:**
            - Default page size: 20
            - Page numbers start from 0
            - Supports sorting by any task field
            
            **Examples:**
            - Get pending tasks: `?status=PENDING`
            - Get tasks for this week: `?startDate=2025-07-07T00:00:00&endDate=2025-07-14T23:59:59`
            - Get completed tasks sorted by date: `?status=COMPLETED&sort=endDate,desc`
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved tasks",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class),
                examples = @ExampleObject(
                    name = "Successful Response",
                    value = """
                        {
                          "content": [
                            {
                              "id": "123e4567-e89b-12d3-a456-426614174000",
                              "title": "Complete project documentation",
                              "description": "Write comprehensive documentation",
                              "startDate": "2025-07-07T09:00:00",
                              "endDate": "2025-07-07T17:00:00",
                              "priority": "HIGH",
                              "status": "PENDING",
                              "recurrenceType": "NONE",
                              "participants": []
                            }
                          ],
                          "totalElements": 1,
                          "totalPages": 1,
                          "size": 20,
                          "number": 0
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Bad Request",
                    value = """
                        {
                          "timestamp": "2025-07-07T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Invalid date format"
                        }
                        """
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @Parameter(
                description = "Pagination and sorting parameters",
                example = "page=0&size=10&sort=startDate,desc"
            )
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,

            @Parameter(
                description = "Filter by task status",
                example = "PENDING",
                schema = @Schema(implementation = TaskStatus.class)
            )
            @RequestParam(required = false) TaskStatus status,

            @Parameter(
                description = "Filter tasks starting after this date (ISO 8601 format)",
                example = "2025-07-07T00:00:00"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(
                description = "Filter tasks ending before this date (ISO 8601 format)",
                example = "2025-07-14T23:59:59"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Getting tasks with filters - status: {}, startDate: {}, endDate: {}, page: {}",
                status, startDate, endDate, pageable);

        Page<TaskDto> tasks = taskService.getAllTasks(pageable, status, startDate, endDate);

        log.info("Found {} tasks out of {} total", tasks.getNumberOfElements(), tasks.getTotalElements());

        return ResponseEntity.ok(tasks);
    }

    @Operation(
        summary = "Get task by ID",
        description = """
            Retrieve detailed information about a specific task by its unique identifier.
            
            Returns complete task information including:
            - Basic task details (title, description, dates)
            - Priority and status information
            - Recurrence pattern (if applicable)
            - Associated participants and their roles
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Task found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TaskDto.class),
                examples = @ExampleObject(
                    name = "Task Details",
                    value = """
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "title": "Complete project documentation",
                          "description": "Write comprehensive documentation for the project",
                          "startDate": "2025-07-07T09:00:00",
                          "endDate": "2025-07-07T17:00:00",
                          "priority": "HIGH",
                          "status": "IN_PROGRESS",
                          "recurrenceType": "WEEKLY",
                          "recurrencePattern": {
                            "id": "456e7890-e89b-12d3-a456-426614174000",
                            "type": "WEEKLY",
                            "interval": 1,
                            "daysOfWeek": ["MONDAY", "WEDNESDAY"],
                            "endDate": "2025-12-31"
                          },
                          "participants": [
                            {
                              "id": "789e0123-e89b-12d3-a456-426614174000",
                              "name": "John Doe",
                              "title": "Senior Developer",
                              "department": "Engineering",
                              "role": "USER"
                            }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Task Not Found",
                    value = """
                        {
                          "timestamp": "2025-07-07T10:30:00",
                          "status": 404,
                          "error": "Not Found",
                          "message": "Task not found with id: 123e4567-e89b-12d3-a456-426614174000"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid UUID format",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Invalid UUID",
                    value = """
                        {
                          "timestamp": "2025-07-07T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Invalid UUID format"
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(
            @Parameter(
                description = "Unique identifier of the task",
                example = "123e4567-e89b-12d3-a456-426614174000",
                required = true
            )
            @PathVariable UUID id) {

        log.info("Getting task by ID: {}", id);

        TaskDto task = taskService.getTaskById(id);

        return ResponseEntity.ok(task);
    }
}
