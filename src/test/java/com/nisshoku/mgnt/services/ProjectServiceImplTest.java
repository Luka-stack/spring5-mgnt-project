package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ProjectServiceImplTest {

    private final Integer ID = 1;
    private final String TITLE = "Some Title";

    private ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    TaskRepository taskRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        projectService = new ProjectServiceImpl(projectRepository, employeeRepository, taskRepository,
                                                ProjectMapper.INSTANCE);
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

    @Test
    public void getProjectById() {

        // given
        Project project = new Project();
        project.setTitle(TITLE);
        project.setId(ID);

        when(projectRepository.findById(ID)).thenReturn(java.util.Optional.of(project));

        // when
        ProjectDTO projectDTO = projectService.getProjectById(ID);

        // then
        assertEquals(TITLE, project.getTitle());
    }

    @Test
    public void getProjectByState() {

        // given
        Project project = new Project();
        project.setId(ID);
        project.setStateOfProject(State.IN_PROGRESS);

        Project project1 = new Project();
        project1.setId(ID + 1);
        project1.setStateOfProject(State.IN_PROGRESS);

        List<Project> projects = Arrays.asList(project, project1);

        when(projectRepository.findByStateOfProject(any())).thenReturn(projects);

        // when
        List<ProjectDTO> projectDTOList = projectService.getProjectsByState("IN_PROGRESS");

        // then
        assertEquals(2, projectDTOList.size());
        assertEquals(State.IN_PROGRESS, projectDTOList.get(0).getStateOfProject());
        assertEquals(State.IN_PROGRESS, projectDTOList.get(1).getStateOfProject());
    }

    @Test
    public void getProjectsByYear() throws Exception {

        Date date = new SimpleDateFormat("yyy-MM-dd").parse("2019-01-01");
        Date date2 = new SimpleDateFormat("yyy-MM-dd").parse("2019-05-05");

        // given
        Project project = new Project();
        project.setId(ID);
        project.setStartDate(date);

        Project project2 = new Project();
        project2.setId(ID);
        project2.setStartDate(date2);

        List<Project> projects = Arrays.asList(project, project2);

        when(projectRepository.findByYear(any(), any())).thenReturn(projects);

        // when
        List<ProjectDTO> projectDTOList = projectService.getProjectsByYear("2019");

        // then
        assertEquals(2, projectDTOList.size());
        assertEquals(date, projectDTOList.get(0).getStartDate());
        assertEquals(date2, projectDTOList.get(1).getStartDate());
    }

    @Test
    public void getProjectByTitle() {

        // given
        Project project = new Project();
        project.setId(ID);
        project.setTitle(TITLE);

        when(projectRepository.findByTitle(anyString())).thenReturn(java.util.Optional.of(project));

        // when
        ProjectDTO projectDTO = projectService.getProjectByTitle(TITLE);

        // then
        assertEquals(TITLE, projectDTO.getTitle());
    }

    @Test
    public void createProject() {

        // given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);

        Project project = new Project();
        project.setId(ID);
        project.setTitle(TITLE);

        when(projectRepository.save(any())).thenReturn(project);

        // when
        ProjectDTO DTOSaved = projectService.createProject(projectDTO);

        // then
        assertEquals(projectDTO.getTitle(), DTOSaved.getTitle());
        assertEquals(ProjectController.URL_BASE + "/1", DTOSaved.getProjectUrl());
    }
}