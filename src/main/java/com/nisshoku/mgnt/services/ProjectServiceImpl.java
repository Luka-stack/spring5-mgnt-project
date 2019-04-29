package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

                    return projectDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Integer id) {

        return projectRepository.findById(id)
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

                    return projectDTO;
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<ProjectDTO> getProjectsByState(State state) {

        return projectRepository.findByStateOfProject(state)
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

                    return projectDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByYear(String year) {

        // Temporal solution
        Date fromDate = null;
        Date tillDate = null;
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-01-01");
            tillDate = new SimpleDateFormat("yyy-MM-dd").parse(year + "-12-31");
        } catch (ParseException e) {
            // TODO implement exception
            throw new RuntimeException("Bad Year");
        }

        return projectRepository.findByYear(fromDate, tillDate)
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

                    return projectDTO;
                })
                .collect(Collectors.toList());

    }

    @Override
    public ProjectDTO getProjectByTitle(String title) {

        return projectRepository.findByTitle(title)
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

                    return projectDTO;
                }).orElseThrow(RuntimeException::new);
    }

    private String getProjectUrl(Integer id) {
        return ProjectController.URL_BASE + "/" + id;
    }
}
