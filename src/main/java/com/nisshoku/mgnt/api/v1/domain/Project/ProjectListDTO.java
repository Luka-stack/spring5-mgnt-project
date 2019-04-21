package com.nisshoku.mgnt.api.v1.domain.Project;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectListDTO {

    List<ProjectDTO> projects;
}
