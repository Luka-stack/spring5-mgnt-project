package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.bootstrap.DataLoader;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    EmployeeService employeeService;

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
    public void updateEmployeeFullBody() {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.setEmail(EMAIL);
        employeeDTO.setFavoriteLanguage(Language.GO);

        Integer getUpdateId = getEmployeeIdValue();
        Employee employeeDB = employeeRepository.getOne(getUpdateId);
        assertNotNull(employeeDB);

        EmployeeDTO savedEmployee = employeeService.updateEmployeeFullBody(getUpdateId, employeeDTO);

        assertNotNull(savedEmployee.getFirstName(), employeeDB.getFirstName());
        assertNotNull(savedEmployee.getLastName(), employeeDB.getLastName());
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
}
