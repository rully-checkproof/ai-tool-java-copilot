package com.checkproof.explore.ai_tool_java_copilot.dto;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Participant information for task assignment")
public class ParticipantDto {

    @Schema(description = "Unique identifier for the participant", example = "456e7890-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Full name of the participant", example = "John Doe", maxLength = 100)
    private String name;

    @Schema(description = "Job title of the participant", example = "Senior Developer", maxLength = 100)
    private String title;

    @Schema(description = "Department the participant belongs to", example = "Engineering", maxLength = 100)
    private String department;

    @Schema(description = "Role of the participant in the system", example = "USER")
    private Role role;
}
