package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private UUID id;
    private String name;
    private String title;
    private Role role;
}

