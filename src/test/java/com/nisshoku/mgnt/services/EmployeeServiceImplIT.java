package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.bootstrap.DataLoader;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
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
public class EmployeeServiceImplIT {

    private final String FIRSTNAME = "FIRST NAME";
    private final String LASTNAME = "LAST NAME";
    private final String EMAIL = "EMAIL@EMAIL.COM";

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    private EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        DataLoader dataLoader = new DataLoader(employeeRepository, projectRepository, taskRepository);
        dataLoader.run();

        employeeService = new EmployeeServiceImpl(EmployeeMapper.INSTANCE, employeeRepository, projectRepository);
    }

    @Test
    public void createNewEmployeeWithExistingProject() {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.setEmail(EMAIL);
        employeeDTO.setFavoriteLanguage(Language.GO);

        Integer projectID = getEmployeeIdValue();
        Project projectDB = projectRepository.getOne(projectID);
        assertNotNull(projectDB);

        EmployeeDTO savedEmployee = employeeService.createNewEmployeeWithExistingProject(projectID, employeeDTO);

        assertEquals(savedEmployee.getFirstName(), FIRSTNAME);
        assertEquals(savedEmployee.getLastName(), LASTNAME);
        assertEquals(1, savedEmployee.getProjects().size());
    }

    @Test
    public void updateEmployee() {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.setEmail(EMAIL);
        employeeDTO.setFavoriteLanguage(Language.GO);

        Integer getUpdateId = getEmployeeIdValue();
        Employee employeeDB = employeeRepository.getOne(getUpdateId);
        assertNotNull(employeeDB);

        String orginalFirst = employeeDB.getFirstName();
        String orginalEmail = employeeDB.getEmail();

        employeeService.updateEmployee(getUpdateId, employeeDTO);

        assertNotEquals(orginalFirst, employeeDB.getFirstName());
        assertNotEquals(orginalEmail, employeeDB.getEmail());
        assertEquals(FIRSTNAME, employeeDB.getFirstName());
        assertEquals(EMAIL, employeeDB.getEmail());
    }

    @Test
    public void patchEmployee() {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);

        Integer getPatchId = getEmployeeIdValue();
        Employee employeeDB = employeeRepository.getOne(getPatchId);
        assertNotNull(employeeDB);

        String orginalName = employeeDB.getFirstName();

        employeeService.patchEmployee(getPatchId, employeeDTO);

        assertNotEquals(orginalName, employeeDB.getFirstName());
        assertEquals(FIRSTNAME, employeeDB.getFirstName());
        assertNotEquals(0, employeeDB.getProjects().size());
    }

    @Test
    public void addProjectToEmployee() {

        Employee employee = getValidEmployee();
        assertNotNull(employee);

        Project project = new Project();
        project.setTitle("Task Title");
        project.setDescription("Task Long Description");
        project.setStateOfProject(State.IN_PROGRESS);
        project.setStartDate(new Date(2019, Calendar.FEBRUARY, 1));

        Project savedProject = projectRepository.save(project);

        int oldProjectsSize = employee.getProjects().size();

        employeeService.addProjectToEmployee(employee.getId(), savedProject.getId());

        assertNotEquals(oldProjectsSize, employee.getProjects().size());
        assertEquals(oldProjectsSize+1, employee.getProjects().size());
    }

    @Test
    public void deleteProjectFromEmployee() {

        int employeeId = getEmployeeIdValue();

        Employee employee = employeeRepository.findById(employeeId).get();
        assertNotNull(employee);
        int oldSize = employee.getProjects().size();

        int projectID = employee.getProjects().stream().findAny().get().getId();
        employeeService.deleteProjectFromEmployee(employeeId, projectID);

        assertNotEquals(oldSize, employee.getProjects().size());
        assertEquals(oldSize-1, employee.getProjects().size());
    }

    @Test
    public void deleteAllProjectsFromEmployee() {

        Integer employeeId = getEmployeeIdValue();
        Employee employee = employeeRepository.getOne(employeeId);
        assertNotNull(employee);

        employeeService.deleteAllProjectsFromEmployee(employeeId);

        assertEquals(0, employee.getProjects().size());
    }

    @Test
    public void deleteEmployeeById() {

        List<Employee> employeesBeforeDelete = employeeRepository.findAll();
        Integer id = employeesBeforeDelete.get(0).getId();

        employeeService.deleteEmployeeById(id);

        List<Employee> employeesAfterDelete = employeeRepository.findAll();


        assertEquals(employeesAfterDelete.size(), employeesBeforeDelete.size()-1);
    }

    private Integer getEmployeeIdValue() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.get(0).getId();
    }

    private Employee getValidEmployee() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.get(0);
    }
}
