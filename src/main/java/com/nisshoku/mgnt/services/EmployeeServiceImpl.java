package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectBaseDTO;
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

import java.util.HashSet;
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

        return employeeRepository.findById(id).map(employee -> {

            EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
            employeeDTO.setEmployeeUrl(getEmployeeUrl(id));
            for (ProjectBaseDTO project : employeeDTO.getProjects())
                project.setProjectUrl(getProjectUrl(project.getTitle()));

            return employeeDTO;
        }).orElseThrow(() ->
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
                    .map(employee -> {
                        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
                        employeeDTO.setEmployeeUrl(getEmployeeUrl(employee.getId()));

                        for (ProjectBaseDTO project : employeeDTO.getProjects())
                            project.setProjectUrl(getProjectUrl(project.getTitle()));

                        return employeeDTO;
                    }).collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Favorite language: "+language+"does not exist in Database");
        }
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

        Employee savedEmployee = employeeRepository.save(employeeMapper.employeeDTOToEmployee(employeeDTO));
        EmployeeDTO returnedDTO = employeeMapper.employeeToEmployeeDTO(savedEmployee);

        returnedDTO.setEmployeeUrl(getEmployeeUrl(savedEmployee.getId()));

        return returnedDTO;
    }

    // TODO you know what you should do
    @Override
    public EmployeeDTO createNewEmployeeWithExistingProject(Integer id, EmployeeDTO employeeDTO) {

        Optional<Project> projectDB = projectRepository.findById(id);
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);

        if (projectDB.isPresent()) {
            Project project = projectDB.get();
            employee.getProjects().add(project);
        }

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

    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employeeDTO) {

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setId(id);

        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeDTO returnedDTO = employeeMapper.employeeToEmployeeDTO(savedEmployee);

        returnedDTO.setEmployeeUrl(getEmployeeUrl(savedEmployee.getId()));
        returnedDTO.getProjects().forEach(project -> project.setProjectUrl(getProjectUrl(project.getTitle())));

        return returnedDTO;
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

            EmployeeDTO returnedDTO = employeeMapper.employeeToEmployeeDTO(employee);
            returnedDTO.setEmployeeUrl(getEmployeeUrl(id));
            returnedDTO.getProjects().forEach(project -> project.setProjectUrl(getProjectUrl(project.getTitle())));

            return returnedDTO;
        }).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+id+" doesn't exist",
                                               EmployeeController.BASE_URL + "/{employee_id}")
        );
    }

    @Override
    public void addProjectToEmployee(Integer employeeId, Integer projectId) {

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
        employeeRepository.save(employee);
    }

    @Override
    public void deleteProjectFromEmployee(Integer employeeId, Integer projectId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/delete_project/{projectId}")
        );
        Project foundProject = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/delete_project/{projectId}")
        );

        employee.getProjects().forEach(project -> {

            if (project.getId().equals(projectId)) {
                project.getEmployees().remove(employee);
                projectRepository.save(project);
            }
        });

        employee.getProjects().remove(foundProject);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteAllProjectsFromEmployee(Integer employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        EmployeeController.BASE_URL + "{employeeId}/clear_projects")
        );

        employee.getProjects().forEach(project -> {
            project.getEmployees().remove(employee);
            projectRepository.save(project);
        });

        employee.setProjects(new HashSet<>());
    }

    @Override
    public void deleteEmployeeById(Integer id) {

        employeeRepository.deleteById(id);
    }

    // TODO you know what you should do
    private String getProjectUrl(String title) {
        return ProjectController.URL_BASE + "/" + title.toLowerCase();
    }

    private String getEmployeeUrl(Integer id) {
        return EmployeeController.BASE_URL + "/" + id;
    }
}
