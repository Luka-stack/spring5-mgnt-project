package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.controllers.v1.EmployeeController;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository,
                               ProjectRepository projectRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
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
    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {

        return saveAndReturnDTO(employeeMapper.employeeDTOToEmployee(employeeDTO));
    }

    @Override
    public EmployeeDTO createNewEmployeeWithExistingProject(Integer id, EmployeeDTO employeeDTO) {

        Optional<Project> projectDB = projectRepository.findById(id);
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);

        if (projectDB.isPresent()) {
            Project project = projectDB.get();
            employee.getProjects().add(project);
        }

        //return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(employee));
        return saveAndReturnDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployeeFullBody(Integer id, EmployeeDTO employeeDTO) {

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setId(id);

        return saveAndReturnDTO(employee);
    }

    @Override
    public EmployeeDTO patchEmployee(Integer id, EmployeeDTO employeeDTO) {

        return employeeRepository.findById(id).map(employee -> {

            if (employeeDTO.getFirstName() != null) {
                employee.setFirstName(employeeDTO.getFirstName());
            }

            if (employeeDTO.getLastName() != null) {
                employee.setLastName(employeeDTO.getLastName());
            }

            if (employeeDTO.getEmail() != null) {
                employee.setEmail(employeeDTO.getEmail());
            }

            if (employeeDTO.getFavoriteLanguage() != null) {
                employee.setFavoriteLanguage(employeeDTO.getFavoriteLanguage());
            }

            if (employeeDTO.getPhoneNumber() != null) {
                employee.setPhoneNumber(employeeDTO.getPhoneNumber());
            }

            EmployeeDTO returnedDTO = employeeMapper.employeeToEmployeeDTO(employee);
            returnedDTO.setEmployeeUrl(getEmployeeUrl(id));

            return returnedDTO;
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteEmployeeById(Integer id) {

        employeeRepository.deleteById(id);
    }

    private EmployeeDTO saveAndReturnDTO(Employee employee) {

        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeDTO returnedDTO = employeeMapper.employeeToEmployeeDTO(savedEmployee);

        returnedDTO.setEmployeeUrl(getEmployeeUrl(savedEmployee.getId()));
        returnedDTO.getProjects().forEach(project -> project.setProjectUrl(getProjectUrl(project.getTitle())));

        if (employee.getProjects() != null && employee.getProjects().size() > 0) {
            employee.getProjects().forEach(project -> {
                employee.setProjects(null);
                project.getEmployees().add(employee);
                projectRepository.save(project);
            });
        }

        return returnedDTO;
    }

    private String getProjectUrl(String title) {
        return ProjectController.URL_BASE + "/" + title.toLowerCase();
    }

    private String getEmployeeUrl(Integer id) {
        return EmployeeController.BASE_URL + "/" + id;
    }
}
