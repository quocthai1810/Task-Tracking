package com.example.task_tracker.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.task_tracker.domain.entities.TaskPriority;
import com.example.task_tracker.domain.entities.TaskStatus;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status) {

}
