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

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO createProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO updateProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        return projectService.updateProject(id, projectDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO patchProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        return projectService.patchProject(id, projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
    }

    @PostMapping("/{projectId}/add_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void addEmployeeToProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        projectService.addEmployeeToProject(projectId, employeeId);
    }

    @PostMapping("/{projectId}/delete_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployeeFromProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        projectService.deleteEmployeeFromProject(projectId, employeeId);
    }

    @PostMapping("{projectId}/clear_employees")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllEmployeesFromProject(@PathVariable Integer projectId) {
        projectService.deleteAllEmployeesFromProject(projectId);
    }

    @DeleteMapping("/{projectId}/delete_task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskFromProject(@PathVariable Integer projectId, @PathVariable Integer taskId) {
        projectService.deleteTaskFromProject(projectId, taskId);
    }

    @DeleteMapping("/{projectId}/clear_tasks")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllTasksFromProject(@PathVariable Integer projectId) {
        projectService.deleteAllTasksFromProject(projectId);
    }

}
