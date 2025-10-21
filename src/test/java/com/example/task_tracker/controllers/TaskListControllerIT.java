package com.example.task_tracker.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.task_tracker.domain.dto.TaskListDto;
import com.example.task_tracker.domain.entities.TaskList;
import com.example.task_tracker.mappers.TaskListMapper;
import com.example.task_tracker.services.TaskListService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskListController.class)
public class TaskListControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskListService taskListService;

    @MockitoBean
    private TaskListMapper taskListMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskList taskList;
    private TaskListDto taskListDto;
    private UUID taskListId;

    @BeforeEach
    void setUp() {
        taskListId = UUID.randomUUID();
        taskList = new TaskList(taskListId, "My List", "Desc", null, null, null);
        taskListDto = new TaskListDto(
                taskListId,
                "My List",
                "Desc",
                0, // count
                0.0, // progress
                List.of() // tasks
        );
    }

    // ===================== listTaskLists =====================
    @Test
    void testListTaskLists() throws Exception {
        when(taskListService.listTaskLists()).thenReturn(List.of(taskList));
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(get("/task-lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(taskListId.toString()))
                .andExpect(jsonPath("$[0].title").value("My List"))
                .andExpect(jsonPath("$[0].description").value("Desc"));
    }

    // ===================== createTaskList =====================
    @Test
    void testCreateTaskList() throws Exception {
        when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
        when(taskListService.createTaskList(taskList)).thenReturn(taskList);
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(post("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("My List"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    // ===================== getTaskList =====================
    @Test
    void testGetTaskList_Found() throws Exception {
        when(taskListService.getTaskList(taskListId)).thenReturn(Optional.of(taskList));
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(get("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("My List"));
    }

    @Test
    void testGetTaskList_NotFound() throws Exception {
        when(taskListService.getTaskList(taskListId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk()) // trả Optional.empty → body rỗng
                .andExpect(content().string("null"));
    }

    // ===================== updateTaskList =====================
    @Test
    void testUpdateTaskList() throws Exception {
        when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
        when(taskListService.updateTaskList(taskListId, taskList)).thenReturn(taskList);
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(put("/task-lists/{task_list_id}", taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("My List"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    // ===================== deleteTaskList =====================
    @Test
    void testDeleteTaskList() throws Exception {
        doNothing().when(taskListService).deleteTaskList(taskListId);

        mockMvc.perform(delete("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk());
    }
}
