package com.nisshoku.mgnt.api.v1.mappers;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeExtDTO;
import com.nisshoku.mgnt.domain.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    EmployeeExtDTO employeeToEmployeeExtDTO(Employee employee);
}
