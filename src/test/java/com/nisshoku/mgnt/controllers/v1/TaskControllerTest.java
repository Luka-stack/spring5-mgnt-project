package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.services.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.yaml.snakeyaml.events.Event;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.nisshoku.mgnt.controllers.AbstractRestControllerTest.asJsonString;

public class TaskControllerTest {

    private final String TITLE = "Task Title";
    /*private final String DESCRIPTION = "Test Description for Task";
    private final State TASK_STATE = State.BUG;*/

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

        when(taskService.getTasksByState("IN_PROGRESS")).thenReturn(dtoList);

        mockMvc.perform(get(TaskController.BASE_URL + "/state/" + State.IN_PROGRESS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(2)))
                .andExpect(jsonPath("$.tasks[0].stateOfTask", equalTo("IN_PROGRESS")))
                .andExpect(jsonPath("$.tasks[1].stateOfTask", equalTo("IN_PROGRESS")));
    }

    @Test
    public void createTask() throws Exception {

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setTitle(TITLE);

        TaskBaseDTO taskReturned = new TaskBaseDTO();
        taskReturned.setTitle(TITLE);

        when(taskService.createTask(any(TaskBaseDTO.class), anyInt())).thenReturn(taskReturned);

        mockMvc.perform(put(TaskController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    // Implement Test For 'AddListOfTasks'
}