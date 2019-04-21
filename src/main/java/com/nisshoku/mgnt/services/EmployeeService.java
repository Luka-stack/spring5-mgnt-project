package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeExtDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();
    EmployeeExtDTO getEmployeeById(Integer id);

    //TODO implement this
    //List<EmployeeDTO> getEmployeesByLanguage();
    //List<EmployeeDTO> getEmployeesByLastName(String lastName);
}
