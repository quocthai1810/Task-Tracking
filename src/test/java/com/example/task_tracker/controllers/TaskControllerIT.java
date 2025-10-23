package com.example.task_tracker.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.task_tracker.domain.dto.TaskDto;
import com.example.task_tracker.domain.entities.Task;
import com.example.task_tracker.domain.entities.TaskList;
import com.example.task_tracker.domain.entities.TaskPriority;
import com.example.task_tracker.domain.entities.TaskStatus;
import com.example.task_tracker.jwt.JwtService;
import com.example.task_tracker.mappers.TaskMapper;
import com.example.task_tracker.services.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID taskListId;
    private UUID taskId;
    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskListId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        TaskList taskList = new TaskList(taskListId, "My List", null, null, null, null);

        task = new Task(taskId, "Task 1", "Desc", null, TaskStatus.OPEN, TaskPriority.MEDIUM,
                taskList, null, null);
        taskDto = new TaskDto(taskId, "Task 1", "Desc", null, TaskPriority.MEDIUM, null);
    }

    // ===================== listTasks =====================
    @Test
    void testListTasks() throws Exception {
        when(taskService.listTasks(taskListId)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/task-lists/{task_list_id}/tasks", taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskId.toString()))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Desc"));
    }

    // ===================== createTask =====================
    @Test
    void testCreateTask() throws Exception {
        when(taskMapper.fromDto(taskDto)).thenReturn(task);
        when(taskService.createTask(taskListId, task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(post("/task-lists/{task_list_id}/tasks", taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    // ===================== getTask =====================
    @Test
    void testGetTask_Found() throws Exception {
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    void testGetTask_NotFound() throws Exception {
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
    }

    // ===================== updateTask =====================
    @Test
    void testUpdateTask() throws Exception {
        when(taskMapper.fromDto(taskDto)).thenReturn(task);
        when(taskService.updateTask(taskListId, taskId, task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(put("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    // ===================== deleteTask =====================
    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(taskListId, taskId);

        mockMvc.perform(delete("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk());
    }
}
