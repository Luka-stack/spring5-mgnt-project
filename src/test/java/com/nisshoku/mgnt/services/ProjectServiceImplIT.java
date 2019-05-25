package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.bootstrap.DataLoader;
import com.nisshoku.mgnt.domain.*;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectServiceImplIT {

    private final String TITLE = "Task Title";
    private final String DESCRIPTION = "Task Long Description";
    private final State PROJECT_STATE = State.IN_PROGRESS;
    private final Date START_DATE = new Date(2019, Calendar.FEBRUARY, 1);

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TaskRepository taskRepository;

    private ProjectService projectService;

    @Before
    public void setUp() throws Exception {
        DataLoader dataLoader = new DataLoader(employeeRepository, projectRepository, taskRepository);
        dataLoader.run();

        projectService = new ProjectServiceImpl(projectRepository, employeeRepository, taskRepository,
                                                ProjectMapper.INSTANCE);
    }

    @Test
    public void updateProject() {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);
        projectDTO.setDescription(DESCRIPTION);
        projectDTO.setStateOfProject(PROJECT_STATE);
        projectDTO.setStartDate(START_DATE);

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        projectService.updateProject(projectDB.getId(), projectDTO);

        assertEquals(TITLE, projectDB.getTitle());
        assertEquals(DESCRIPTION, projectDB.getDescription());
    }

    @Test
    public void patchProject() {

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(TITLE);

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        int employeesSize = projectDB.getEmployees().size();
        int tasksSize = projectDB.getTasks().size();

        projectService.patchProject(projectDB.getId(), projectDTO);

        assertEquals(projectDTO.getTitle(), projectDB.getTitle());
        assertEquals(employeesSize, projectDB.getEmployees().size());
        assertEquals(tasksSize, projectDB.getTasks().size());
    }

    @Test
    public void deleteProjectById() {

        List<Project> beforeList = projectRepository.findAll();
        assertNotNull(beforeList);

        projectService.deleteProjectById(beforeList.get(0).getId());

        List<Project> afterList = projectRepository.findAll();
        assertNotNull(afterList);

        assertEquals(beforeList.size() - 1, afterList.size());
    }

    @Test
    public void addEmployeeToProject() {

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        Employee employee = new Employee();
        employee.setFirstName("FIRST NAME");
        employee.setLastName("LAST NAME");
        employee.setEmail("EMAIL@EMAIL.COM");
        employee.setFavoriteLanguage(Language.GO);

        Employee savedEmployee = employeeRepository.save(employee);

        int oldEmployeeSize = projectDB.getEmployees().size();

        projectService.addEmployeeToProject(projectDB.getId(), savedEmployee.getId());

        assertNotEquals(projectDB.getEmployees().size(), oldEmployeeSize);
        assertEquals(oldEmployeeSize + 1, projectDB.getEmployees().size());
    }

    @Test
    public void deleteEmployeeFromProject() {

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        Optional<Employee> employee = projectDB.getEmployees().stream().findAny();
        Employee employeeFound = employee.get();
        int oldEmployeeSize = projectDB.getEmployees().size();

        projectService.deleteEmployeeFromProject(projectDB.getId(), employeeFound.getId());

        assertEquals(oldEmployeeSize - 1, projectDB.getEmployees().size());
    }

    @Test
    public void deleteAllEmployeesFromProject() {

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        projectService.deleteAllEmployeesFromProject(projectDB.getId());

        assertEquals(0, projectDB.getEmployees().size());
    }

    @Test
    public void deleteTaskFromProject() {

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        Optional<Task> task = projectDB.getTasks().stream().findAny();
        Task taskFound = task.get();
        int oldEmployeeSize = projectDB.getEmployees().size();

        projectService.deleteTaskFromProject(projectDB.getId(), taskFound.getId());

        assertEquals(oldEmployeeSize-1, projectDB.getTasks().size());
    }

    @Test
    public void deleteAllTasksFromProject() throws Exception {

        Project projectDB = getValidProject();
        assertNotNull(projectDB);

        projectService.deleteAllTasksFromProject(projectDB.getId());

        assertEquals(0, projectDB.getTasks().size());
    }

    private Project getValidProject() {

        List<Project> projects = projectRepository.findAll();

        return projects.get(0);
    }

    private Integer getValidId() {

        List<Project> projects = projectRepository.findAll();

        return projects.get(0).getId();
    }

}
