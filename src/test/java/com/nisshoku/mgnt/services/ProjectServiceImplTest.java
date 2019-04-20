package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ProjectServiceImplTest {

    private ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        projectService = new ProjectServiceImpl(projectRepository, ProjectMapper.INSTANCE);
    }

    @Test
    public void getAllProjects() {

        // given
        List<Project> projects = Arrays.asList(new Project(), new Project());

        when(projectRepository.findAll()).thenReturn(projects);

        // when
        List<ProjectDTO> projectDTOList = projectService.getAllProjects();

        // then
        assertEquals(2, projectDTOList.size());
    }
}