package com.checkproof.explore.ai_tool_java_copilot.repository;

import com.checkproof.explore.ai_tool_java_copilot.domain.Task;
import com.checkproof.explore.ai_tool_java_copilot.enumeration.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

  public static Specification<Task> withFilters(TaskStatus status,
      LocalDateTime startDate,
      LocalDateTime endDate) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (status != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), status));
      }

      if (startDate != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
      }

      if (endDate != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
      }

      query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

}
