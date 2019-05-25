package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
import com.nisshoku.mgnt.domain.Language;
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

import static com.nisshoku.mgnt.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void creatNewEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.createNewEmployee(any())).thenReturn(returnedDTO);

        mockMvc.perform(post(EmployeeController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(0)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    public void createNewEmployeeWithExistingProject() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);

        ProjectBaseDTO projectBaseDTO = new ProjectBaseDTO();

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(projectBaseDTO);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.createNewEmployeeWithExistingProject(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(post(EmployeeController.BASE_URL + "/project/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    public void updateEmployee() throws Exception {


        ProjectBaseDTO project = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.setLastName(LASTNAME);
        employeeDTO.getProjects().add(project);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(project);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.updateEmployee(anyInt(), any())).thenReturn(returnedDTO);

        mockMvc.perform(put(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    public void patchEmployee() throws Exception {

        ProjectBaseDTO project = new ProjectBaseDTO();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(FIRSTNAME);
        employeeDTO.getProjects().add(project);

        EmployeeDTO returnedDTO = new EmployeeDTO();
        returnedDTO.setFirstName(FIRSTNAME + 123);
        returnedDTO.setLastName(LASTNAME);
        returnedDTO.getProjects().add(project);
        returnedDTO.setEmployeeUrl(EmployeeController.BASE_URL + "/1");

        when(employeeService.patchEmployee(anyInt(), any(EmployeeDTO.class))).thenReturn(returnedDTO);

        mockMvc.perform(patch(EmployeeController.BASE_URL + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRSTNAME + 123)))
                .andExpect(jsonPath("$.lastName", equalTo(LASTNAME)))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.employeeUrl", equalTo(EmployeeController.BASE_URL + "/1")));
    }

    @Test
    public void deleteProjectById() throws Exception {

        mockMvc.perform(delete(EmployeeController.BASE_URL +  "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteEmployeeById(anyInt());
    }

    @Test
    public void addProjectToEmployee() throws Exception {

        mockMvc.perform(post(EmployeeController.BASE_URL +  "/1/add_project/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).addProjectToEmployee(anyInt(), anyInt());
    }

    @Test
    public void deleteProjectFromEmployee() throws Exception {

        mockMvc.perform(delete(EmployeeController.BASE_URL +  "/1/delete_project/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteProjectFromEmployee(anyInt(), anyInt());
    }

    @Test
    public void deleteAllProjects() throws Exception {

        mockMvc.perform(delete(EmployeeController.BASE_URL +  "/1/clear_projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteAllProjectsFromEmployee(anyInt());
    }
}