package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.domain.ProjectSharedDTO;
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

                    for (ProjectSharedDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    private String getProjectUrl(String title) {
        return ProjectController.URL_BASE + "/" + title.toLowerCase();
    }
}
