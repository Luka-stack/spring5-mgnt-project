package com.nisshoku.mgnt.api.v1.domain.employee;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExtDTO extends EmployeeBaseDTO {

    private String phoneNumber;
    Set<ProjectDTO> projects = new HashSet<>();
}
