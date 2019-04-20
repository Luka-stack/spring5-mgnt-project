package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

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
                .map(projectMapper::projectToProjectDTO)
                .collect(Collectors.toList());
    }
}
