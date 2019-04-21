package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.services.EmployeeService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    private final String FIRSTNAME = "FirstName";
    private final String LASTNAME = "LastName";

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController employeeController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void getAllEmployees() throws Exception {

        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setFirstName(FIRSTNAME);
        employeeDTO1.setLastName(LASTNAME);

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setFirstName(FIRSTNAME + 2);
        employeeDTO2.setLastName(LASTNAME + 2);

        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);

        when(employeeService.getAllEmployees()).thenReturn(employeeDTOList);

        mockMvc.perform(get(EmployeeController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees", hasSize(2)));

    }

    @Test
    public void getEmployeeById() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        when(employeeService.getEmployeeById(anyInt())).thenReturn(employeeDTO);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)));
    }
}