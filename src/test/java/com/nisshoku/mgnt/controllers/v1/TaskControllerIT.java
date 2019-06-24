package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
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
    public void getTaskById() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        when(taskService.getTaskById(anyInt())).thenReturn(taskDTO);

        mockMvc.perform(get(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    @Test
    @WithMockUser(username = "empployee", roles = "EMPLOYEE")
    public void getTaskById_NotFound() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        when(taskService.getTaskById(anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTaskById_isUnauthorized() throws Exception {

        mockMvc.perform(get(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    public void getAllTasks() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setTitle(TITLE + "123");

        List<TaskDTO> dtoList = Arrays.asList(taskDTO, taskDTO2);

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

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStateOfTask(State.IN_PROGRESS);

        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setStateOfTask(State.IN_PROGRESS);

        List<TaskDTO> dtoList = Arrays.asList(taskDTO, taskDTO2);

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

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        TaskDTO taskReturned = new TaskDTO();
        taskReturned.setTitle(TITLE);

        when(taskService.createTask(anyInt(), any(TaskDTO.class))).thenReturn(taskReturned);

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
    public void updateTask() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        TaskDTO taskReturned = new TaskDTO();
        taskReturned.setTitle(TITLE);

        when(taskService.updateTask(anyInt(), any(TaskDTO.class))).thenReturn(taskReturned);

        mockMvc.perform(put(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    @Test
    public void updateTask_isUnauthorized() throws Exception {

        mockMvc.perform(put(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void patchTask() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        TaskDTO taskReturned = new TaskDTO();
        taskReturned.setTitle(TITLE);

        when(taskService.patchTask(anyInt(), any(TaskDTO.class))).thenReturn(taskReturned);

        mockMvc.perform(patch(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void patchTask_NotFound() throws Exception {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        when(taskService.patchTask(anyInt(), any(TaskDTO.class))).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(patch(TaskController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(taskDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchTask_isUnauthorized() throws Exception {

        mockMvc.perform(patch(TaskController.BASE_URL + "/1")
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
