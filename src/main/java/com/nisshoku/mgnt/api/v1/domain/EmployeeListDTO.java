package com.nisshoku.mgnt.api.v1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeListDTO {

    List<EmployeeDTO> employees;
}
