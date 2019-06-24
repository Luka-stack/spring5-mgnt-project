package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.services.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.nisshoku.mgnt.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    private final String TITLE = "Task Title";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
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
    public void getAllTasks_isUnauthorized() throws Exception {

        mockMvc.perform(get(TaskController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
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
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    public void getTasksByState_isConflict() throws Exception {

        when(taskService.getTasksByState(anyString())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get(TaskController.BASE_URL + "/state/asdasd")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getTasksByState_isUnauthorized() throws Exception {

        mockMvc.perform(get(TaskController.BASE_URL + "/state/" + State.IN_PROGRESS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    public void createTask() throws Exception {

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setTitle(TITLE);

        TaskBaseDTO taskReturned = new TaskBaseDTO();
        taskReturned.setTitle(TITLE);

        when(taskService.createTask(any(TaskBaseDTO.class), anyInt())).thenReturn(taskReturned);

        mockMvc.perform(post(TaskController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    @Test
    public void createTask_isUnauthorized() throws Exception {

        mockMvc.perform(post(TaskController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteTaskById() throws Exception {

        mockMvc.perform(delete(TaskController.BASE_URL +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(taskService).deleteTaskById(anyInt());
    }

    @Test
    public void deleteTaskById_isUnauthorized() throws Exception {

        mockMvc.perform(delete(TaskController.BASE_URL +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
