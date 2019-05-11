package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeListDTO;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EmployeeController.BASE_URL)
public class EmployeeController {

    public static final String BASE_URL = "/api/v1/employees";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeListDTO getAllEmployees() {
        return new EmployeeListDTO(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getEmployeeById(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/lang/{language}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeListDTO getEmployeesByLanguage(@PathVariable("language") Language language) {
        return new EmployeeListDTO(employeeService.getEmployeesByLanguage(language));
    }

    @GetMapping("/lastname/{lastName}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeListDTO getEmployeesByLastName(@PathVariable String lastName) {
        return new EmployeeListDTO(employeeService.getEmployeesByLastName(lastName));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createNewEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createNewEmployee(employeeDTO);
    }

    @PostMapping("/project/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createNewEmployeeWithExistingProject(@RequestBody EmployeeDTO employeeDTO,
                                                            @PathVariable Integer id) {
        return employeeService.createNewEmployeeWithExistingProject(id, employeeDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Integer id) {
        return employeeService.updateEmployee(id, employeeDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO patchEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Integer id) {
        return employeeService.patchEmployee(id, employeeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployeeById(id);
    }
}
