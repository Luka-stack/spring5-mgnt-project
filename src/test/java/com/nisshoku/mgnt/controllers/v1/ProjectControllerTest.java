package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.services.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.nisshoku.mgnt.controllers.AbstractRestControllerTest.asJsonString;

public class ProjectControllerTest {

    private final String TITLE = "BigTitle";
    private final String DESCRIPTION = "LongDescription";

    @Mock
    ProjectService projectService;

    @InjectMocks
    ProjectController projectController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    public void getAllProjects() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);

        ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setTitle(TITLE + 2);
        projectDTO2.setDescription(DESCRIPTION + 2);

        List<ProjectDTO> projectDTOList = Arrays.asList(projectDTO, projectDTO2);

        when(projectService.getAllProjects()).thenReturn(projectDTOList);

        mockMvc.perform(get(ProjectController.URL_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects", hasSize(2)));
    }

    @Test
    public void getProjectById() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);

        when(projectService.getProjectById(anyInt())).thenReturn(projectDTO);

        mockMvc.perform(get(ProjectController.URL_BASE + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));
    }

    @Test
    public void getProjectsByState() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setStateOfProject(State.DONE);

        ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setStateOfProject(State.DONE);

        List<ProjectDTO> projectDTOList = Arrays.asList(projectDTO, projectDTO2);

        when(projectService.getProjectsByState(any())).thenReturn(projectDTOList);

        mockMvc.perform(get(ProjectController.URL_BASE + "/state/" + State.DONE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects", hasSize(2)))
                .andExpect(jsonPath("$.projects[0].stateOfProject", equalTo("DONE")))
                .andExpect(jsonPath("$.projects[1].stateOfProject", equalTo("DONE")));
    }

    @Test
    public void getProjectsByYear() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setStartDate(new SimpleDateFormat("yyy-MM-dd").parse("2019-01-01"));

        ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setStartDate(new SimpleDateFormat("yyy-MM-dd").parse("2019-05-05"));

        List<ProjectDTO> projectDTOList =  Arrays.asList(projectDTO, projectDTO2);

        when(projectService.getProjectsByYear("2019")).thenReturn(projectDTOList);

        mockMvc.perform(get(ProjectController.URL_BASE + "/year/2019")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects", hasSize(2)));
    }

    @Test
    public void getProjectByTitle() throws Exception {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);

        when(projectService.getProjectByTitle(anyString())).thenReturn(projectDTO);

        mockMvc.perform(get(ProjectController.URL_BASE + "/title/" + TITLE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)));

    }

    @Test
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
                .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(TITLE)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.projectUrl", equalTo(ProjectController.URL_BASE + "/1")));
    }

    @Test
    public void deleteProjectById() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteProjectById(anyInt());
    }

    @Test
    public void addEmployeeToProject() throws Exception {

        mockMvc.perform(post(ProjectController.URL_BASE +  "/1/add_employee/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).addEmployeeToProject(anyInt(), anyInt());
    }

    @Test
    public void deleteEmployeeFromProject() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1/delete_employee/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteEmployeeFromProject(anyInt(), anyInt());
    }

    @Test
    public void deleteAllEmployeesFromProject() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1/clear_employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteAllEmployeesFromProject(anyInt());
    }

    @Test
    public void deleteTaskFromProject() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1/delete_task/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteTaskFromProject(anyInt(), anyInt());
    }

    @Test
    public void deleteAllTasksFromProject() throws Exception {

        mockMvc.perform(delete(ProjectController.URL_BASE +  "/1/clear_tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(projectService).deleteAllTasksFromProject(anyInt());
    }
}