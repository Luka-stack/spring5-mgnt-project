package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectListDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectListDTO getProjectsByState(@PathVariable State state) {
        return new ProjectListDTO(projectService.getProjectsByState(state));
    }

    @GetMapping("/year/{year}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectListDTO getProjectsByYear(@PathVariable String year) {
        return new ProjectListDTO(projectService.getProjectsByYear(year));
    }

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO getProjectByTitle(@PathVariable String title) {
        return projectService.getProjectByTitle(title);
    }

}
