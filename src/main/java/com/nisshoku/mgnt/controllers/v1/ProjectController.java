package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectListDTO;
import com.nisshoku.mgnt.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProjectController.URL_BASE)
public class ProjectController {

    public static final String URL_BASE = "/api/v1/projects";

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectListDTO getAllProjects() {
        return new ProjectListDTO(projectService.getAllProjects());
    }
}
