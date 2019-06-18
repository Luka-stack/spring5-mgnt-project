package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.exceptions.RestResponseEntityExceptionHandler;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    private final String FIRSTNAME = "FirstName";
    private final String LASTNAME = "LastName";

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                                 .setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
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
    public void getEmployeesByLanguage() throws Exception {

        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setFavoriteLanguage(Language.GO);

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setFavoriteLanguage(Language.GO);

        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);

        when(employeeService.getEmployeesByLanguage(any())).thenReturn(employeeDTOList);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/lang/" + Language.GO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees", hasSize(2)))
                .andExpect(jsonPath("$.employees[0].favoriteLanguage", equalTo("GO")))
                .andExpect(jsonPath("$.employees[1].favoriteLanguage", equalTo("GO")));
    }

    @Test
    public void getEmployeesByLanguageError() throws Exception {

        when(employeeService.getEmployeesByLanguage(anyString())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/lang/qwe")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getEmployeesByLastName() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setLastName(LASTNAME);

        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setLastName(LASTNAME);

        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO, employeeDTO1);

        when(employeeService.getEmployeesByLastName(anyString())).thenReturn(employeeDTOList);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/lastname/" + LASTNAME)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employees", hasSize(2)))
                .andExpect(jsonPath("$.employees[0].lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.employees[1].lastName", equalTo(LASTNAME)));
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

    @Test
    public void getEmployeeByIdNotFound() throws Exception {

        when(employeeService.getEmployeeById(anyInt())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/1111")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}