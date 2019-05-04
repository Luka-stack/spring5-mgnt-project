package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

        Integer projectID = 1;
        Project projectDB = projectRepository.getOne(projectID);
        assertNotNull(projectDB);

        EmployeeDTO savedEmployee = employeeService.createNewEmployeeWithExistingProject(projectID, employeeDTO);

        assertEquals(savedEmployee.getFirstName(), FIRSTNAME);
        assertEquals(savedEmployee.getLastName(), LASTNAME);
        assertEquals(1, savedEmployee.getProjects().size());
    }
}
