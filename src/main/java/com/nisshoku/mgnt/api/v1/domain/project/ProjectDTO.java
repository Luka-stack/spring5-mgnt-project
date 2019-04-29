package com.nisshoku.mgnt.api.v1.domain.project;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
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
public class ProjectDTO extends ProjectBaseDTO{

    Set<EmployeeBaseDTO> employees = new HashSet<>();
    Set<TaskBaseDTO> tasks = new HashSet<>();
}
