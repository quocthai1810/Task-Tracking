package com.example.task_tracker.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.task_tracker.domain.entities.TaskList;
import com.example.task_tracker.repositories.TaskListRepository;

@ExtendWith(MockitoExtension.class)
public class TaskListServiceImplTests {

    @Mock
    private TaskListRepository taskListRepository;

    @InjectMocks
    private TaskListServiceImpl taskListService;

    // ===================== listTaskLists =====================
    @Test
    void testListTaskLists() {
        TaskList tl1 = new TaskList();
        TaskList tl2 = new TaskList();
        when(taskListRepository.findAll()).thenReturn(List.of(tl1, tl2));

        List<TaskList> result = taskListService.listTaskLists();

        assertEquals(2, result.size());
        verify(taskListRepository).findAll();
    }

    // ===================== createTaskList =====================
    @Test
    void testCreateTaskList_HappyFlow() {
        TaskList input = new TaskList(null, "Title", "Desc", null, null, null);
        TaskList saved = new TaskList(UUID.randomUUID(), "Title", "Desc", null, LocalDateTime.now(),
                LocalDateTime.now());
        when(taskListRepository.save(any(TaskList.class))).thenReturn(saved);

        TaskList result = taskListService.createTaskList(input);

        assertNotNull(result.getId());
        assertEquals("Title", result.getTitle());
        assertEquals("Desc", result.getDescription());
        verify(taskListRepository).save(any(TaskList.class));
    }

    @Test
    void testCreateTaskList_AlreadyHasId_ShouldThrow() {
        TaskList input = new TaskList(UUID.randomUUID(), "Title", "Desc", null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskListService.createTaskList(input));
        assertEquals("Task List already has an ID!", ex.getMessage());
    }

    @Test
    void testCreateTaskList_NoTitle_ShouldThrow() {
        TaskList input = new TaskList(null, "", "Desc", null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskListService.createTaskList(input));
        assertEquals("Task List title must be present!", ex.getMessage());
    }

    // ===================== getTaskList =====================
    @Test
    void testGetTaskList_Found() {
        UUID id = UUID.randomUUID();
        TaskList tl = new TaskList(id, "Title", "Desc", null, null, null);
        when(taskListRepository.findById(id)).thenReturn(Optional.of(tl));

        Optional<TaskList> result = taskListService.getTaskList(id);

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());
    }

    @Test
    void testGetTaskList_NotFound() {
        UUID id = UUID.randomUUID();
        when(taskListRepository.findById(id)).thenReturn(Optional.empty());

        Optional<TaskList> result = taskListService.getTaskList(id);

        assertTrue(result.isEmpty());
    }

    // ===================== updateTaskList =====================
    @Test
    void testUpdateTaskList_HappyFlow() {
        UUID id = UUID.randomUUID();
        TaskList input = new TaskList(id, "New Title", "New Desc", null, null, null);
        TaskList existing = new TaskList(id, "Old Title", "Old Desc", null, LocalDateTime.now(), LocalDateTime.now());

        when(taskListRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskListRepository.save(any(TaskList.class))).thenReturn(existing);

        TaskList result = taskListService.updateTaskList(id, input);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        verify(taskListRepository).save(existing);
    }

    @Test
    void testUpdateTaskList_IdMismatch_ShouldThrow() {
        UUID id = UUID.randomUUID();
        TaskList input = new TaskList(UUID.randomUUID(), "Title", "Desc", null, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskListService.updateTaskList(id, input));
        assertEquals("Attempting to change task list ID, this is not permitted!", ex.getMessage());
    }

    @Test
    void testUpdateTaskList_TaskListNotFound_ShouldThrow() {
        UUID id = UUID.randomUUID();
        TaskList input = new TaskList(id, "Title", "Desc", null, null, null);
        when(taskListRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> taskListService.updateTaskList(id, input));
        assertEquals("Task list not found!", ex.getMessage());
    }

    // ===================== deleteTaskList =====================
    @Test
    void testDeleteTaskList() {
        UUID id = UUID.randomUUID();

        taskListService.deleteTaskList(id);

        verify(taskListRepository).deleteById(id);
    }
}
