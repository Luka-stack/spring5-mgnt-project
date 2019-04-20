package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();
    //TODO implement this
    //List<EmployeeBaseDTO> getEmployeesByLanguage();
    //List<EmployeeBaseDTO> getEmployeesByLastName(String lastName);

    //EmployeeBaseDTO getEmployeeById(Integer id);
}
