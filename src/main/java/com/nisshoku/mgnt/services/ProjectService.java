package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects();
}
