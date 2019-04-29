package com.nisshoku.mgnt.api.v1.domain.employee;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends EmployeeBaseDTO {

    Set<ProjectBaseDTO> projects = new HashSet<>();
}
