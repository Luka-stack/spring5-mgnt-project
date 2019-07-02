package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.domain.project.ProjectListDTO;
import com.nisshoku.mgnt.services.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProjectController.URL_BASE)
@Api(description = "Operate on 'Project' entity")
public class ProjectController {

    public static final String URL_BASE = "/api/v1/projects";

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return All Projects")
    public ProjectListDTO getAllProjects() {
        return new ProjectListDTO(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Project With Given ID")
    public ProjectDTO getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Projects With Given State")
    public ProjectListDTO getProjectsByState(@PathVariable String state) {
        return new ProjectListDTO(projectService.getProjectsByState(state));
    }

    @GetMapping("/year/{year}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Projects With Given Year")
    public ProjectListDTO getProjectsByYear(@PathVariable String year) {
        return new ProjectListDTO(projectService.getProjectsByYear(year));
    }

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Project With Given Title")
    public ProjectDTO getProjectByTitle(@PathVariable String title) {
        return projectService.getProjectByTitle(title);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New Project",
            notes = "Require 'admin' role",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO createProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Project With Given ID",
            notes = "Require 'admin' role. Create New Project if given ID doesn't exist in DB. " +
                    "Does Not Clear Employeess or Tasks",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO updateProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        return projectService.updateProject(id, projectDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch Project With Given ID",
            notes = "Require 'admin' role.",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO patchProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        return projectService.patchProject(id, projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete Project With Given ID",
            notes = "Require 'admin' role.",
            authorizations = {@Authorization(value = "basic_auth")})
    public void deleteProject(@PathVariable Integer id) {
        projectService.deleteProjectById(id);
    }

    @PutMapping("/{projectId}/add_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Employee To Project",
            notes = "Require 'admin' role and employee (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO addEmployeeToProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        return projectService.addEmployeeToProject(projectId, employeeId);
    }

    @PutMapping("/{projectId}/delete_employee/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Employee From Project",
            notes = "Require 'admin' role and employee (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO deleteEmployeeFromProject(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        return projectService.deleteEmployeeFromProject(projectId, employeeId);
    }

    @PutMapping("{projectId}/clear_employees")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove All Employees From Project",
            notes = "Require 'admin' role.",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO deleteAllEmployeesFromProject(@PathVariable Integer projectId) {
        return projectService.deleteAllEmployeesFromProject(projectId);
    }

    @PutMapping("/{projectId}/delete_task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Task From Project",
            notes = "Require 'admin' role and task (id). It also deletes that Task.",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO deleteTaskFromProject(@PathVariable Integer projectId, @PathVariable Integer taskId) {
        return projectService.deleteTaskFromProject(projectId, taskId);
    }

    @PutMapping("/{projectId}/clear_tasks")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove All Tasks From Project",
            notes = "Require 'admin' role. It also deletes that Tasks.",
            authorizations = {@Authorization(value = "basic_auth")})
    public ProjectDTO deleteAllTasksFromProject(@PathVariable Integer projectId) {
        return projectService.deleteAllTasksFromProject(projectId);
    }

}
