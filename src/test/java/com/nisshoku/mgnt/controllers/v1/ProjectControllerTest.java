package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.ProjectDTO;
import com.nisshoku.mgnt.services.ProjectService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}