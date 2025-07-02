package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.RecurrenceType;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskPriority;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@ToString
public class Task {

  @Id
  private UUID id;

  private String title;
  private String description;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  @Enumerated(EnumType.STRING)
  private TaskPriority priority;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Enumerated(EnumType.STRING)
  private RecurrenceType recurrenceType;

  @ManyToOne
  @JoinColumn(name = "recurrence_pattern_id")
  private RecurrencePattern recurrencePattern;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
    name = "task_participants",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "participant_id")
  )
  private List<Participant> participants;

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Version
  private Long version;

}
