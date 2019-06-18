package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectListDTO;
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
    public ProjectListDTO getProjectsByState(@PathVariable String state) {
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

    @PutMapping("/{projectId}/add_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO addEmployeeToProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        return projectService.addEmployeeToProject(projectId, employeeId);
    }

    @PutMapping("/{projectId}/delete_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO deleteEmployeeFromProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        return projectService.deleteEmployeeFromProject(projectId, employeeId);
    }

    @PutMapping("{projectId}/clear_employees")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO deleteAllEmployeesFromProject(@PathVariable Integer projectId) {
        return projectService.deleteAllEmployeesFromProject(projectId);
    }

    @PutMapping("/{projectId}/delete_task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO deleteTaskFromProject(@PathVariable Integer projectId, @PathVariable Integer taskId) {
        return projectService.deleteTaskFromProject(projectId, taskId);
    }

    @PutMapping("/{projectId}/clear_tasks")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO deleteAllTasksFromProject(@PathVariable Integer projectId) {
        return projectService.deleteAllTasksFromProject(projectId);
    }

}
