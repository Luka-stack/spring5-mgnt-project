package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeExtDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeListDTO;
import com.nisshoku.mgnt.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EmployeeController.BASE_URL)
public class EmployeeController {

    static final String BASE_URL = "/api/v1/employees";

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
    public EmployeeExtDTO getEmployeeById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }
}
