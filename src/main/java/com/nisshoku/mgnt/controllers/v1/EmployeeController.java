package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.EmployeeListDTO;
import com.nisshoku.mgnt.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
