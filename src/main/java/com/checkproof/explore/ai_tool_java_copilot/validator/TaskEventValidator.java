package com.checkproof.explore.ai_tool_java_copilot.validator;

import com.checkproof.explore.ai_tool_java_copilot.dto.TaskEventMessageDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TaskEventValidator {

    public static boolean isValidTaskEvent(TaskEventMessageDto dto) {
        if (dto == null) {
            log.warn("Received null task event DTO");
            return false;
        }

        if (dto.getId() == null) {
            log.warn("Received task event DTO with null ID: {}", dto);
            return false;
        }

        return true;
    }

    public static void logValidationWarning(TaskEventMessageDto dto) {
        log.warn("Received null or invalid task event DTO: {}", dto);
    }
}
