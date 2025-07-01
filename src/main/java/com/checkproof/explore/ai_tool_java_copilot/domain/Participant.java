package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.Role;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
  private UUID id;
  private String name;
  private String title;
  private Role role;
}
