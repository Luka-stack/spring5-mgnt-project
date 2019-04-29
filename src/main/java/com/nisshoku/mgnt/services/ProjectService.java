package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectDTO;
import com.nisshoku.mgnt.domain.State;

import java.util.List;

public interface ProjectService {

    ProjectDTO getProjectById(Integer id);

    List<ProjectDTO> getAllProjects();

    List<ProjectDTO> getProjectsByState(State state);

    List<ProjectDTO> getProjectsByYear(String year);

    ProjectDTO getProjectByTitle(String title);
}
