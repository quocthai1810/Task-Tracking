package com.example.task_tracker.mappers.impl;

import org.springframework.stereotype.Component;

import com.example.task_tracker.domain.dto.TaskDto;
import com.example.task_tracker.domain.entities.Task;
import com.example.task_tracker.mappers.TaskMapper;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task fromDto(TaskDto taskDto) {
        return new Task(taskDto.id(), taskDto.title(), taskDto.description(), taskDto.dueDate(), taskDto.status(),
                taskDto.priority(),
                null, null, null);
    }

    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(), task.getPriority(),
                task.getStatus());
    }

}
