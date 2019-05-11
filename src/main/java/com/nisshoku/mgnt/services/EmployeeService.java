package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.domain.Language;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO getEmployeeById(Integer id);

    List<EmployeeDTO> getAllEmployees();

    List<EmployeeDTO> getEmployeesByLanguage(Language language);

    List<EmployeeDTO> getEmployeesByLastName(String lastName);

    EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO createNewEmployeeWithExistingProject(Integer id, EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO);

    EmployeeDTO patchEmployee(Integer id, EmployeeDTO employeeDTO);

    void deleteEmployeeById(Integer id);
}
