package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.controllers.v1.EmployeeController;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.Language;
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
                    employeeDTO.setEmployeeUrl(getEmployeeUrl(employee.getId()));

                    for (ProjectBaseDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByLanguage(Language language) {

        return employeeRepository.findByFavoriteLanguage(language)
                .stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
                    employeeDTO.setEmployeeUrl(getEmployeeUrl(employee.getId()));

                    for (ProjectBaseDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesByLastName(String lastName) {

        return employeeRepository.findByLastName(lastName)
                .stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
                    employeeDTO.setEmployeeUrl(getEmployeeUrl(employee.getId()));

                    for (ProjectBaseDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {

        return employeeRepository.findById(id)
                .map(employee -> {
                    EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
                    employeeDTO.setEmployeeUrl(getEmployeeUrl(employee.getId()));

                    for (ProjectBaseDTO project : employeeDTO.getProjects())
                        project.setProjectUrl(getProjectUrl(project.getTitle()));

                    return employeeDTO;
                })
                .orElseThrow(RuntimeException::new);
    }

    private String getProjectUrl(String title) {
        return ProjectController.URL_BASE + "/" + title.toLowerCase();
    }

    private String getEmployeeUrl(Integer id) {
        return EmployeeController.BASE_URL + "/" + id;
    }
}
