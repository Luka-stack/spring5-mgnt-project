package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.Project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeExtDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;


    public EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

                    for (ProjectBaseDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeExtDTO getEmployeeById(Integer id) {

        return employeeRepository.findById(id)
                .map(employee -> {
                    EmployeeExtDTO employeeExtDTO = employeeMapper.employeeToEmployeeExtDTO(employee);

                    for (ProjectDTO project : employeeExtDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeExtDTO;
                })
                .orElseThrow(RuntimeException::new);
    }

    private String getProjectUrl(String title) {
        return ProjectController.URL_BASE + "/" + title.toLowerCase();
    }
}
