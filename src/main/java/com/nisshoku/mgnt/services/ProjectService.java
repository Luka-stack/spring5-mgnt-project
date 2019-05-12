package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.domain.State;

import java.util.List;

public interface ProjectService {

    ProjectDTO getProjectById(Integer id);

    ProjectDTO getProjectByTitle(String title);

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Integer id, ProjectDTO projectDTO);

    ProjectDTO patchProject(Integer id, ProjectDTO projectDTO);

    List<ProjectDTO> getAllProjects();

    List<ProjectDTO> getProjectsByState(State state);

    List<ProjectDTO> getProjectsByYear(String year);

    void deleteProjectById(Integer id);
}
