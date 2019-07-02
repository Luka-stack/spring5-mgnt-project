package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.employee.EmployeeDTO;
import com.nisshoku.mgnt.api.v1.mappers.EmployeeMapper;
import com.nisshoku.mgnt.controllers.v1.EmployeeController;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .map(this::setAllUrl)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {

        return employeeRepository.findById(id).map(this::setAllUrl).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+id+" doesn't exist",
                EmployeeController.BASE_URL + "/{employee_id}")
        );
    }

    @Override
    public List<EmployeeDTO> getEmployeesByLanguage(String language) {

        try {
            Language languageSearch = Language.valueOf(language.toUpperCase());

            return employeeRepository.findByFavoriteLanguage(languageSearch)
                    .stream()
                    .map(this::setAllUrl).collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Favorite language: "+language+"does not exist in Database");
        }
    }

    @Override
    public List<EmployeeDTO> getEmployeesByLastName(String lastName) {

        return employeeRepository.findByLastName(lastName)
                .stream()
                .map(this::setAllUrl).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {

        Employee savedEmployee = employeeRepository.save(employeeMapper.employeeDTOToEmployee(employeeDTO));

        return setAllUrl(savedEmployee);
    }

    @Override
    public EmployeeDTO createNewEmployeeWithExistingProject(Integer id, EmployeeDTO employeeDTO) {

        Project projectDB = projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ id +" doesn't exist",
                        EmployeeController.BASE_URL + "/project/{projectID}")
        );

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.getProjects().add(projectDB);

        Employee savedEmployee = employeeRepository.save(employee);

        if (employee.getProjects() != null && employee.getProjects().size() > 0) {
            employee.getProjects().forEach(project -> {
                employee.setProjects(null);
                project.getEmployees().add(employee);
                projectRepository.save(project);
            });
        }

        return setAllUrl(savedEmployee);
    }

    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO) {

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setId(id);

        Employee savedEmployee = employeeRepository.save(employee);

        return setAllUrl(savedEmployee);
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

            employeeRepository.save(employee);

            return setAllUrl(employee);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+id+" doesn't exist",
                                               EmployeeController.BASE_URL + "/{employee_id}")
        );
    }

    @Override
    public EmployeeDTO addProjectToEmployee(Integer employeeId, Integer projectId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                        new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                                EmployeeController.BASE_URL + "{employeeId}/add_project/{projectId}")
                );
        Project foundProject = projectRepository.findById(projectId).orElseThrow(() ->
                        new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                            EmployeeController.BASE_URL + "{employeeId}/add_project/{projectId}")
                );

        employee.getProjects().add(foundProject);
        foundProject.getEmployees().add(employee);

        projectRepository.save(foundProject);
        Employee savedEmployee = employeeRepository.save(employee);

        return setAllUrl(savedEmployee);
    }

    @Override
    public EmployeeDTO deleteProjectFromEmployee(Integer employeeId, Integer projectId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/delete_project/{projectId}")
        );
        Project foundProject = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/delete_project/{projectId}")
        );

        foundProject.getEmployees().remove(employee);
        employee.getProjects().remove(foundProject);
        projectRepository.save(foundProject);

        Employee savedEmployee = employeeRepository.save(employee);

        return setAllUrl(savedEmployee);
    }

    @Override
    public EmployeeDTO deleteAllProjectsFromEmployee(Integer employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/clear_projects")
        );

        employee.getProjects().forEach(project -> {
            project.getEmployees().remove(employee);
            projectRepository.save(project);
        });

        //employee.setProjects(new HashSet<>());
        employee.getProjects().clear();

        return setAllUrl(employee);
    }

    @Override
    public void deleteEmployeeById(Integer id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ id +" doesn't exist",
                        EmployeeController.BASE_URL + "/{projectId}")
        );

        employeeRepository.delete(employee);
    }

    private EmployeeDTO setAllUrl(Employee employee) {

        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
        employeeDTO.setEmployeeUrl(ProjectController.URL_BASE + "/" + employee.getId());
        employeeDTO.getProjects().forEach(projectDTO -> projectDTO.setProjectUrl(ProjectController.URL_BASE + "/" + projectDTO.getId()));

        return employeeDTO;
    }
}
