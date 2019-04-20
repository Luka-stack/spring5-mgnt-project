package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();
    //TODO implement this
    //List<EmployeeDTO> getEmployeesByLanguage();
    //List<EmployeeDTO> getEmployeesByLastName(String lastName);

    //EmployeeDTO getEmployeeById(Integer id);
}
