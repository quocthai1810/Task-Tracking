package com.example.task_tracker.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.task_tracker.domain.entities.TaskList;

public interface TaskListService {
    List<TaskList> listTaskLists();
    TaskList createTaskList(TaskList taskList);
    Optional<TaskList> getTaskList(UUID id);
}
