package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EmployeeServiceImplTest {

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
}