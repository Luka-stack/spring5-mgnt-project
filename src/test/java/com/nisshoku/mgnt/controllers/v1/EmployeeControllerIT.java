package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.services.EmployeeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    private final String FIRSTNAME = "FirstName";
    private final String LASTNAME = "LastName";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void creatNewEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.createNewEmployee(any())).thenReturn(returnedDTO);

        mockMvc.perform(post(EmployeeController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(0)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    public void creatNewEmployee_isUnauthorized() throws Exception {

        mockMvc.perform(post(EmployeeController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewEmployeeWithExistingProject() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(projectBaseDTO);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.createNewEmployeeWithExistingProject(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(post(EmployeeController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createNewEmployeeWithExistingProject_NotFound() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        when(employeeService.createNewEmployeeWithExistingProject(anyInt(), any()))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(post(EmployeeController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNewEmployeeWithExistingProject_isUnauthorized() throws Exception {
        mockMvc.perform(post(EmployeeController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateEmployee() throws Exception {


        ProjectBaseDTO project = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(project);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(project);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.updateEmployee(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void patchEmployee_NotFound() throws Exception {

        ProjectBaseDTO project = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.getProjects().add(project);

        when(employeeService.patchEmployee(anyInt(), any(EmployeeDTO.class))).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(patch(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchEmployee_isUnauthorized() throws Exception {

        mockMvc.perform(patch(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteEmployeeById() throws Exception {

        mockMvc.perform(delete(EmployeeController.BASE_URL +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteEmployeeById(anyInt());
    }

    @Test
    public void deleteEmployeeById_isUnauthorized() throws Exception {

        mockMvc.perform(delete(EmployeeController.BASE_URL +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addProjectToEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(projectBaseDTO);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.addProjectToEmployee(anyInt(), anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/add_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void addProjectToEmployee_NotFound() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        when(employeeService.addProjectToEmployee(anyInt(), anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/add_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addProjectToEmployee_isUnauthorized() throws Exception {

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/add_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteProjectFromEmployee() throws Exception {

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(projectBaseDTO);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.deleteProjectFromEmployee(anyInt(), anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/delete_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(0)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteProjectFromEmployee_NotFound() throws Exception {

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(projectBaseDTO);

        when(employeeService.deleteProjectFromEmployee(anyInt(), anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/delete_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProjectFromEmployee_isUnauthorized() throws Exception {

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/delete_project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllProjects() throws Exception {

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(projectBaseDTO);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.deleteAllProjectsFromEmployee(anyInt())).thenReturn(returnedDTO);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/clear_projects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(0)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteAllProjects_NotFound() throws Exception {

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(projectBaseDTO);

        when(employeeService.deleteAllProjectsFromEmployee(anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/clear_projects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteAllProjects_isUnauthorized() throws Exception {

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1/clear_projects")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
