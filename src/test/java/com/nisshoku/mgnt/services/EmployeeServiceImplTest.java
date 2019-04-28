package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class EmployeeServiceImplTest {

    private final Integer ID = 1;
    private final String FIRSTNAME = "FirstName";
    private final String LASTNAME = "LastName";

    private EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        employeeService = new EmployeeServiceImpl(EmployeeMapper.INSTANCE, employeeRepository);
    }

    @Test
    public void getAllEmployees() {

        // given
        List<Employee> employees = Arrays.asList(new Employee(), new Employee(), new Employee());

        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        List<EmployeeDTO> employeeDTOList = employeeService.getAllEmployees();

        // then
        assertEquals(3, employeeDTOList.size());
    }

    @Test
    public void getEmployeeById() {

        // given
        Employee employee = new Employee();
        employee.setId(ID);
        employee.setFirstName(FIRSTNAME);
        employee.setLastName(LASTNAME);

        when(employeeRepository.findById(anyInt())).thenReturn(java.util.Optional.of(employee));

        // when
        EmployeeDTO returnedEmployee = employeeService.getEmployeeById(ID);

        // then
        assertEquals(FIRSTNAME, returnedEmployee.getFirstName());
        assertEquals(LASTNAME, returnedEmployee.getLastName());
    }

    @Test
    public void getEmployeesByLanguage() {

        // given
        Employee employee = new Employee();
        employee.setId(ID);
        employee.setFavoriteLanguage(Language.GO);

        Employee employee2 = new Employee();
        employee2.setId(ID + 1);
        employee2.setFavoriteLanguage(Language.GO);

        List<Employee> employees = Arrays.asList(employee, employee2);

        when(employeeRepository.findByFavoriteLanguage(any())).thenReturn(employees);

        // when
        List<EmployeeDTO> returnedEmpoyee = employeeService.getEmployeesByLanguage(Language.GO);

        //then
        assertEquals(2, returnedEmpoyee.size());
        assertEquals(Language.GO, returnedEmpoyee.get(0).getFavoriteLanguage());
        assertEquals(Language.GO, returnedEmpoyee.get(1).getFavoriteLanguage());
    }

    @Test
    public void getEmployeesByLastNaem() {

        // given
        Employee employee = new Employee();
        employee.setId(ID);
        employee.setLastName(LASTNAME);

        Employee employee1 = new Employee();
        employee1.setId(ID);
        employee1.setLastName(LASTNAME);

        List<Employee> employees = Arrays.asList(employee, employee1);

        when(employeeRepository.findByLastName(anyString())).thenReturn(employees);

        // when
        List<EmployeeDTO> employeeDTOList = employeeService.getEmployeesByLastName(LASTNAME);

        // then
        assertEquals(2, employeeDTOList.size());
        assertEquals(LASTNAME, employeeDTOList.get(0).getLastName());
        assertEquals(LASTNAME, employeeDTOList.get(1).getLastName());
    }
}