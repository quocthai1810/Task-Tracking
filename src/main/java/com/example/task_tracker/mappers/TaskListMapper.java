package com.example.task_tracker.mappers;

import com.example.task_tracker.domain.dto.TaskListDto;
import com.example.task_tracker.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
