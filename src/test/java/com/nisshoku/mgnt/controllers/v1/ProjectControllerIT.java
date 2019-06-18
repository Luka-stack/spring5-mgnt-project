package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.services.ProjectService;
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

import static com.nisshoku.mgnt.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerIT {

    private final String TITLE = "BigTitle";
    private final String DESCRIPTION = "LongDescription";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createProject() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.createProject(any())).thenReturn(returnedDTO);

        mockMvc.perform(post(ProjectController.URL_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateProject() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.updateProject(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    public void updateProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void patchProject() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.patchProject(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(patch(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    //TODO DEAL WITH BAD REQUEST

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void patchProject_NotFound() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        when(projectService.patchProject(anyInt(), any(ProjectDTO.class))).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(patch(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchProject_isUnauthorized() throws Exception {

        mockMvc.perform(patch(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteProjectById() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteProjectById(anyInt());
    }

    //TODO DO I NEED NOTFOUND?

    @Test
    public void deleteProjectById_isUnauthorized() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addEmployeeToProject() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        EmployeeBaseDTO employeeBaseDTO = new EmployeeBaseDTO();

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.getEmployees().add(employeeBaseDTO);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.addEmployeeToProject(anyInt(), anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/add_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.employees", hasSize(1)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addEmployeeToProject_NotFound() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        when(projectService.addEmployeeToProject(anyInt(), anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/add_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addEmployeeToProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/add_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteEmployeeFromProject() throws Exception {

        EmployeeBaseDTO employeeBaseDTO = new EmployeeBaseDTO();

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.getEmployees().add(employeeBaseDTO);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteEmployeeFromProject(anyInt(), anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.employees", hasSize(0)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteEmployeeFromProject_NotFound() throws Exception {

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteEmployeeFromProject(anyInt(), anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployeeFromProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_employee/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllEmployeesFromProject() throws Exception {

        EmployeeBaseDTO employeeBaseDTO = new EmployeeBaseDTO();

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.getEmployees().add(employeeBaseDTO);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteAllEmployeesFromProject(anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.employees", hasSize(0)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllEmployeesFromProject_NotFound() throws Exception {

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteAllEmployeesFromProject(anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteAllEmployeesFromProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_employees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteTaskFromProject() throws Exception {

        TaskBaseDTO taskBaseDTO = new TaskBaseDTO();

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.getTasks().add(taskBaseDTO);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteTaskFromProject(anyInt(), anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_task/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.tasks", hasSize(0)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteTaskFromProject_NotFound() throws Exception {

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteTaskFromProject(anyInt(), anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_task/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTaskFromProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/delete_task/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllTasksFromProject() throws Exception {

        TaskBaseDTO taskBaseDTO = new TaskBaseDTO();

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.getTasks().add(taskBaseDTO);

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteAllTasksFromProject(anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.tasks", hasSize(0)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllTasksFromProject_NotFound() throws Exception {

        ProjectDTO returnedDTO = new ProjectDTO();
        returnedDTO.setTitle(TITLE);
        returnedDTO.setDescription(DESCRIPTION);
        returnedDTO.setProjectUrl(ProjectController.URL_BASE + "/1");

        when(projectService.deleteAllTasksFromProject(anyInt())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnedDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteAllTasksFromProject_isUnauthorized() throws Exception {

        mockMvc.perform(put(ProjectController.URL_BASE + "/1/clear_tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
