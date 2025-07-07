package com.checkproof.explore.ai_tool_java_copilot.service.impl;

import com.checkproof.explore.ai_tool_java_copilot.domain.Participant;
import com.checkproof.explore.ai_tool_java_copilot.domain.RecurrencePattern;
import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.dto.ParticipantDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.RecurrencePatternDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskDto;
import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.Role;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskPriority;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import com.checkproof.explore.ai_tool_java_copilot.repository.ParticipantRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.RecurrencePatternRepository;
import com.checkproof.explore.ai_tool_java_copilot.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImpl Tests")
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RecurrencePatternRepository recurrencePatternRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskId;
    private UUID recurrencePatternId;
    private UUID participantId;
    private TaskEventMessageDto taskEventDto;
    private Task task;
    private RecurrencePattern recurrencePattern;
    private Participant participant;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        recurrencePatternId = UUID.randomUUID();
        participantId = UUID.randomUUID();

        // Setup test data
        recurrencePattern = RecurrencePattern.builder()
                .id(recurrencePatternId)
                .type(RecurrenceType.WEEKLY)
                .recurrenceInterval(1)
                .daysOfWeek(Arrays.asList("MONDAY", "WEDNESDAY"))
                .endDate(LocalDate.now().plusMonths(6))
                .build();

        participant = Participant.builder()
                .id(participantId)
                .name("John Doe")
                .title("Developer")
                .department("Engineering")
                .role(Role.USER)
                .build();

        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(2))
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .recurrenceType(RecurrenceType.WEEKLY)
                .recurrencePattern(recurrencePattern)
                .participants(List.of(participant))
                .build();

        taskEventDto = TaskEventMessageDto.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(2))
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.PENDING)
                .recurrenceType(RecurrenceType.WEEKLY)
                .recurrencePattern(RecurrencePatternDto.builder()
                        .id(recurrencePatternId)
                        .type(RecurrenceType.WEEKLY)
                        .interval(1)
                        .daysOfWeek(Arrays.asList("MONDAY", "WEDNESDAY"))
                        .endDate(LocalDate.now().plusMonths(6))
                        .build())
                .participants(List.of(ParticipantDto.builder()
                        .id(participantId)
                        .name("John Doe")
                        .title("Developer")
                        .department("Engineering")
                        .role(Role.USER)
                        .build()))
                .build();
    }

    @Nested
    @DisplayName("handleTaskEvent Tests")
    class HandleTaskEventTests {

        @Test
        @DisplayName("Should handle valid task event for new task successfully")
        void shouldHandleValidTaskEventForNewTask() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(recurrencePatternRepository.findById(recurrencePatternId))
                        .thenReturn(Optional.of(recurrencePattern));
                when(participantRepository.findById(participantId))
                        .thenReturn(Optional.of(participant));
                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verify(recurrencePatternRepository).findById(recurrencePatternId);
                verify(participantRepository).findById(participantId);
                verify(taskRepository).findById(taskId);
                verify(taskRepository).save(any(Task.class));
            }
        }

        @Test
        @DisplayName("Should handle valid task event for existing task successfully")
        void shouldHandleValidTaskEventForExistingTask() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(recurrencePatternRepository.findById(recurrencePatternId))
                        .thenReturn(Optional.of(recurrencePattern));
                when(participantRepository.findById(participantId))
                        .thenReturn(Optional.of(participant));
                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.of(task));

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.updateTaskEntity(task, taskEventDto))
                        .thenAnswer(invocation -> null); // void method
                when(taskRepository.save(task))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verify(recurrencePatternRepository).findById(recurrencePatternId);
                verify(participantRepository).findById(participantId);
                verify(taskRepository).findById(taskId);
                verify(taskRepository).save(task);
                mapper.verify(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.updateTaskEntity(task, taskEventDto));
            }
        }

        @Test
        @DisplayName("Should handle invalid task event and return early")
        void shouldHandleInvalidTaskEventAndReturnEarly() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(false);
                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.logValidationWarning(taskEventDto))
                        .thenAnswer(invocation -> null); // void method

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                validator.verify(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.logValidationWarning(taskEventDto));
                verifyNoInteractions(taskRepository);
                verifyNoInteractions(recurrencePatternRepository);
                verifyNoInteractions(participantRepository);
            }
        }

        @Test
        @DisplayName("Should create new recurrence pattern when ID not found")
        void shouldCreateNewRecurrencePatternWhenIdNotFound() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(recurrencePatternRepository.findById(recurrencePatternId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toRecurrencePatternEntity(taskEventDto.getRecurrencePattern()))
                        .thenReturn(recurrencePattern);
                when(recurrencePatternRepository.saveAndFlush(recurrencePattern))
                        .thenReturn(recurrencePattern);
                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verify(recurrencePatternRepository).findById(recurrencePatternId);
                verify(recurrencePatternRepository).saveAndFlush(recurrencePattern);
                verify(taskRepository).save(any(Task.class));
            }
        }

        @Test
        @DisplayName("Should create new recurrence pattern when ID is null")
        void shouldCreateNewRecurrencePatternWhenIdIsNull() {
            // Given
            taskEventDto.getRecurrencePattern().setId(null);

            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toRecurrencePatternEntity(taskEventDto.getRecurrencePattern()))
                        .thenReturn(recurrencePattern);
                when(recurrencePatternRepository.saveAndFlush(recurrencePattern))
                        .thenReturn(recurrencePattern);
                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verify(recurrencePatternRepository, never()).findById(any());
                verify(recurrencePatternRepository).saveAndFlush(recurrencePattern);
                verify(taskRepository).save(any(Task.class));
            }
        }

        @Test
        @DisplayName("Should handle task event without recurrence pattern")
        void shouldHandleTaskEventWithoutRecurrencePattern() {
            // Given
            taskEventDto.setRecurrencePattern(null);

            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verifyNoInteractions(recurrencePatternRepository);
                verify(taskRepository).save(any(Task.class));
            }
        }

        @Test
        @DisplayName("Should create new participant when ID not found")
        void shouldCreateNewParticipantWhenIdNotFound() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(participantRepository.findById(participantId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toParticipantEntity(taskEventDto.getParticipants().get(0)))
                        .thenReturn(participant);
                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verify(participantRepository).findById(participantId);
                mapper.verify(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toParticipantEntity(taskEventDto.getParticipants().get(0)));
                verify(taskRepository).save(any(Task.class));
            }
        }

        @Test
        @DisplayName("Should handle task event without participants")
        void shouldHandleTaskEventWithoutParticipants() {
            // Given
            taskEventDto.setParticipants(null);

            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator> validator =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.class);
                 MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.class)) {

                validator.when(() -> com.checkproof.explore.ai_tool_java_copilot.validator.TaskEventValidator.isValidTaskEvent(taskEventDto))
                        .thenReturn(true);

                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.empty());
                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskEventMapper.toTaskEntity(taskEventDto))
                        .thenReturn(task);
                when(taskRepository.save(any(Task.class)))
                        .thenReturn(task);

                // When
                taskService.handleTaskEvent(taskEventDto);

                // Then
                verifyNoInteractions(participantRepository);
                verify(taskRepository).save(any(Task.class));
            }
        }
    }

    @Nested
    @DisplayName("getAllTasks Tests")
    class GetAllTasksTests {

        @Test
        @DisplayName("Should get all tasks with filters successfully")
        void shouldGetAllTasksWithFiltersSuccessfully() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            TaskStatus status = TaskStatus.PENDING;
            LocalDateTime startDate = LocalDateTime.now().minusDays(7);
            LocalDateTime endDate = LocalDateTime.now();

            List<Task> tasks = List.of(task);
            Page<Task> taskPage = new PageImpl<>(tasks, pageable, 1);

            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.class)) {

                when(taskRepository.findTasksWithFilters(pageable, status, startDate, endDate))
                        .thenReturn(taskPage);

                TaskDto taskDto = TaskDto.builder()
                        .id(taskId)
                        .title("Test Task")
                        .description("Test Description")
                        .build();

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.toTaskDto(task))
                        .thenReturn(taskDto);

                // When
                Page<TaskDto> result = taskService.getAllTasks(pageable, status, startDate, endDate);

                // Then
                assertThat(result).isNotNull();
                assertThat(result.getContent()).hasSize(1);
                assertThat(result.getContent().get(0).getId()).isEqualTo(taskId);
                verify(taskRepository).findTasksWithFilters(pageable, status, startDate, endDate);
            }
        }

        @Test
        @DisplayName("Should get all tasks without filters successfully")
        void shouldGetAllTasksWithoutFiltersSuccessfully() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);

            List<Task> tasks = List.of(task);
            Page<Task> taskPage = new PageImpl<>(tasks, pageable, 1);

            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.class)) {

                when(taskRepository.findTasksWithFilters(pageable, null, null, null))
                        .thenReturn(taskPage);

                TaskDto taskDto = TaskDto.builder()
                        .id(taskId)
                        .title("Test Task")
                        .description("Test Description")
                        .build();

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.toTaskDto(task))
                        .thenReturn(taskDto);

                // When
                Page<TaskDto> result = taskService.getAllTasks(pageable, null, null, null);

                // Then
                assertThat(result).isNotNull();
                assertThat(result.getContent()).hasSize(1);
                verify(taskRepository).findTasksWithFilters(pageable, null, null, null);
            }
        }

        @Test
        @DisplayName("Should return empty page when no tasks found")
        void shouldReturnEmptyPageWhenNoTasksFound() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<Task> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(taskRepository.findTasksWithFilters(pageable, null, null, null))
                    .thenReturn(emptyPage);

            // When
            Page<TaskDto> result = taskService.getAllTasks(pageable, null, null, null);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
            verify(taskRepository).findTasksWithFilters(pageable, null, null, null);
        }
    }

    @Nested
    @DisplayName("getTaskById Tests")
    class GetTaskByIdTests {

        @Test
        @DisplayName("Should get task by ID successfully")
        void shouldGetTaskByIdSuccessfully() {
            // Given
            try (MockedStatic<com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper> mapper =
                 mockStatic(com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.class)) {

                when(taskRepository.findById(taskId))
                        .thenReturn(Optional.of(task));

                TaskDto taskDto = TaskDto.builder()
                        .id(taskId)
                        .title("Test Task")
                        .description("Test Description")
                        .build();

                mapper.when(() -> com.checkproof.explore.ai_tool_java_copilot.util.TaskDtoMapper.toTaskDto(task))
                        .thenReturn(taskDto);

                // When
                TaskDto result = taskService.getTaskById(taskId);

                // Then
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(taskId);
                assertThat(result.getTitle()).isEqualTo("Test Task");
                verify(taskRepository).findById(taskId);
            }
        }

        @Test
        @DisplayName("Should throw exception when task not found")
        void shouldThrowExceptionWhenTaskNotFound() {
            // Given
            when(taskRepository.findById(taskId))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> taskService.getTaskById(taskId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Task not found with id: " + taskId);

            verify(taskRepository).findById(taskId);
        }
    }
}
