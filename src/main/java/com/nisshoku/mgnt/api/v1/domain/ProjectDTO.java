package com.nisshoku.mgnt.api.v1.domain;

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
public class ProjectDTO extends ProjectBaseDTO {

    private Set<EmployeeBaseDTO> employees = new HashSet<>();
}
