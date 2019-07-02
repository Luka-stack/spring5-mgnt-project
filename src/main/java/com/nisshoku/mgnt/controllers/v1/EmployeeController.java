package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeListDTO;
import com.nisshoku.mgnt.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EmployeeController.BASE_URL)
@Api(description = "Operate on 'Employee' entity")
public class EmployeeController {

    public static final String BASE_URL = "/api/v1/employees";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return All Employees")
    public EmployeeListDTO getAllEmployees() {
        return new EmployeeListDTO(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Employee With Given ID")
    public EmployeeDTO getEmployeeById(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/lang/{language}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Employees With Given Language")
    public EmployeeListDTO getEmployeesByLanguage(@PathVariable String language) {
        return new EmployeeListDTO(employeeService.getEmployeesByLanguage(language));
    }

    @GetMapping("/lastname/{lastName}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Employees With Given Last Name")
    public EmployeeListDTO getEmployeesByLastName(@PathVariable String lastName) {
        return new EmployeeListDTO(employeeService.getEmployeesByLastName(lastName));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New Employee", notes = "Require 'admin' role",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createNewEmployee(employeeDTO);
    }

    @PostMapping("/project/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New Employee With Existing Project", notes = "Require 'admin' role and project (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO createNewEmployeeWithExistingProject(@RequestBody EmployeeDTO employeeDTO,
                                                            @PathVariable Integer id) {
        return employeeService.createNewEmployeeWithExistingProject(id, employeeDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Updated Employee With Given Id",
            notes = "Require 'admin' role. Create new employee if given ID doesn't exist in DB. " +
                    "Does Not Clear Projects",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Integer id) {
        return employeeService.updateEmployee(id, employeeDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch Employee With Given ID", notes = "Require 'admin' role",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO patchEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Integer id) {
        return employeeService.patchEmployee(id, employeeDTO);
    }

    @PutMapping("{employeeId}/add_project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add Project To Employee", notes = "Require 'admin' role and project (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO addProjectToEmployee(@PathVariable Integer employeeId, @PathVariable Integer projectId) {
        return employeeService.addProjectToEmployee(employeeId, projectId);
    }

    @PutMapping("{employeeId}/delete_project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove Project From Employee", notes = "Require 'admin' role and project (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO deleteProjectFromEmployee(@PathVariable Integer employeeId, @PathVariable Integer projectId) {
        return employeeService.deleteProjectFromEmployee(employeeId, projectId);
    }

    @PutMapping("{employeeId}/clear_projects")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove All Projects From Employee", notes = "Require 'admin' role",
            authorizations = {@Authorization(value = "basic_auth")})
    public EmployeeDTO deleteAllProjects(@PathVariable Integer employeeId) {
        return employeeService.deleteAllProjectsFromEmployee(employeeId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete Employee With Given ID", notes = "Require 'admin' role",
            authorizations = {@Authorization(value = "basic_auth")})
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }
}
