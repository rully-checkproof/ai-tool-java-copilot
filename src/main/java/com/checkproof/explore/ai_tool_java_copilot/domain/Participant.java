package com.checkproof.explore.ai_tool_java_copilot.domain;

import com.checkproof.explore.ai_tool_java_copilot.enumeration.Role;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {

    @Id
    private UUID id;

    private String name;
    private String title;
    private String department;

    @Enumerated(EnumType.STRING)
    private Role role;
}
