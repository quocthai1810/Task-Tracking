package com.example.task_tracker.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.task_tracker.domain.entities.*;
import com.example.task_tracker.repositories.TaskListRepository;
import com.example.task_tracker.repositories.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskListRepository taskListRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskListId;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskListId = UUID.randomUUID();
        taskList = new TaskList(taskListId, "My List", null, null, LocalDateTime.now(), LocalDateTime.now());
    }

    // ===================== listTasks =====================
    @Test
    void testListTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskRepository.findByTaskListId(taskListId)).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.listTasks(taskListId);

        assertEquals(2, result.size());
        verify(taskRepository).findByTaskListId(taskListId);
    }

    // ===================== createTask =====================
    @Test
    void testCreateTask_HappyFlow() {
        Task input = new Task(null, "Task 1", "Desc", null, null, null, null, null, null);
        Task saved = new Task(UUID.randomUUID(), "Task 1", "Desc", null, TaskStatus.OPEN, TaskPriority.MEDIUM,
                taskList, LocalDateTime.now(), LocalDateTime.now());

        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result = taskService.createTask(taskListId, input);

        assertNotNull(result.getId());
        assertEquals("Task 1", result.getTitle());
        assertEquals("Desc", result.getDescription());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
        assertEquals(TaskStatus.OPEN, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testCreateTask_AlreadyHasId_ShouldThrow() {
        Task input = new Task(UUID.randomUUID(), "Task 1", null, null, null, null, null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(taskListId, input));
        assertEquals("Task already has an ID!", ex.getMessage());
    }

    @Test
    void testCreateTask_NoTitle_ShouldThrow() {
        Task input = new Task(null, "", "Desc", null, null, null, null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(taskListId, input));
        assertEquals("Task must have an title", ex.getMessage());
    }

    @Test
    void testCreateTask_InvalidTaskListId_ShouldThrow() {
        Task input = new Task(null, "Task 1", "Desc", null, null, null, null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.createTask(taskListId, input));
        assertEquals("Invalid Task List ID provided!", ex.getMessage());
    }

    // ===================== getTask =====================
    @Test
    void testGetTask_Found() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task(taskId, "Task", "Desc", null, TaskStatus.OPEN, TaskPriority.HIGH,
                taskList, LocalDateTime.now(), LocalDateTime.now());
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTask(taskListId, taskId);

        assertTrue(result.isPresent());
        assertEquals("Task", result.get().getTitle());
    }

    @Test
    void testGetTask_NotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTask(taskListId, taskId);

        assertTrue(result.isEmpty());
    }

    // ===================== updateTask =====================
    @Test
    void testUpdateTask_HappyFlow() {
        UUID taskId = UUID.randomUUID();
        Task input = new Task(taskId, "Updated Task", "Updated Desc", null, TaskStatus.OPEN, TaskPriority.HIGH,
                taskList, null, LocalDateTime.now());

        Task existing = new Task(taskId, "Old Task", "Old Desc", null, TaskStatus.OPEN, TaskPriority.MEDIUM,
                taskList, LocalDateTime.now(), LocalDateTime.now());

        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(existing);

        Task result = taskService.updateTask(taskListId, taskId, input);

        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        verify(taskRepository).save(existing);
    }

    @Test
    void testUpdateTask_IdMismatch_ShouldThrow() {
        UUID taskId = UUID.randomUUID();
        Task input = new Task(UUID.randomUUID(), "Task", "Desc", null, TaskStatus.OPEN, TaskPriority.HIGH,
                taskList, null, LocalDateTime.now());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, input));
        assertEquals("Task IDs do not match!", ex.getMessage());
    }

    @Test
    void testUpdateTask_TaskNotFound_ShouldThrow() {
        UUID taskId = UUID.randomUUID();
        Task input = new Task(taskId, "Task", "Desc", null, TaskStatus.OPEN, TaskPriority.HIGH,
                taskList, null, LocalDateTime.now());

        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, input));
        assertEquals("Task not found!", ex.getMessage());
    }

    @Test
    void testUpdateTask_NullPriority_ShouldThrow() {
        UUID taskId = UUID.randomUUID();
        Task input = new Task(taskId, "Task", "Desc", null, TaskStatus.OPEN, null,
                taskList, null, LocalDateTime.now());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, input));
        assertEquals("Task must have a valid priority!", ex.getMessage());
    }

    @Test
    void testUpdateTask_NullStatus_ShouldThrow() {
        UUID taskId = UUID.randomUUID();
        Task input = new Task(taskId, "Task", "Desc", null, null, TaskPriority.HIGH,
                taskList, null, LocalDateTime.now());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, input));
        assertEquals("Task must have a valid status!", ex.getMessage());
    }

    // ===================== deleteTask =====================
    @Test
    void testDeleteTask() {
        UUID taskId = UUID.randomUUID();

        taskService.deleteTask(taskListId, taskId);

        verify(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);
    }
}
