package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO getProjectById(Integer id);

    ProjectDTO getProjectByTitle(String title);

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Integer id, ProjectDTO projectDTO);

    ProjectDTO patchProject(Integer id, ProjectDTO projectDTO);

    List<ProjectDTO> getAllProjects();

    List<ProjectDTO> getProjectsByState(String state);

    List<ProjectDTO> getProjectsByYear(String year);

    void deleteProjectById(Integer id);

    void addEmployeeToProject(Integer projectId, Integer employeeId);

    void deleteEmployeeFromProject(Integer projectId, Integer employeeId);

    void deleteAllEmployeesFromProject(Integer projectId);

    void deleteTaskFromProject(Integer projectId, Integer taskId);

    void deleteAllTasksFromProject(Integer projectId);
}
