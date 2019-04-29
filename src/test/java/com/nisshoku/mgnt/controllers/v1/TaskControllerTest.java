package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.services.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest {

    private final String TITLE = "Task Title";

    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController taskController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void getAllTasks() throws Exception {

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setTitle(TITLE);

        TaskBaseDTO taskDTO2 = new TaskBaseDTO();
        taskDTO2.setTitle(TITLE + "123");

        List<TaskBaseDTO> dtoList = Arrays.asList(taskDTO, taskDTO2);

        when(taskService.getAllTasks()).thenReturn(dtoList);

        mockMvc.perform(get(TaskController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)));
    }

    @Test
    public void getTasksByState() throws Exception {

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setStateOfTask(State.IN_PROGRESS);

        TaskBaseDTO taskDTO2 = new TaskBaseDTO();
        taskDTO2.setStateOfTask(State.IN_PROGRESS);

        List<TaskBaseDTO> dtoList = Arrays.asList(taskDTO, taskDTO2);

        when(taskService.getTasksByState(State.IN_PROGRESS)).thenReturn(dtoList);

        mockMvc.perform(get(TaskController.BASE_URL + "/state/" + State.IN_PROGRESS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)))
                .andExpect(jsonPath("$.tasks[0].stateOfTask", equalTo("IN_PROGRESS")))
                .andExpect(jsonPath("$.tasks[1].stateOfTask", equalTo("IN_PROGRESS")));
    }

    //TODO implement better task
    @Test
    public void getNotDoneTasks() throws Exception {

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setStateOfTask(State.BUG);

        TaskBaseDTO taskDTO2 = new TaskBaseDTO();
        taskDTO2.setStateOfTask(State.ACCEPTED);

        List<TaskBaseDTO> dtoList = Arrays.asList(taskDTO, taskDTO2);

        when(taskService.getNotDoneTasks()).thenReturn(dtoList);

        mockMvc.perform(get(TaskController.BASE_URL + "/notdone/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)));
    }
}