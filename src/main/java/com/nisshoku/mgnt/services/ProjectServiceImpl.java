package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.controllers.v1.EmployeeController;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.controllers.v1.TaskController;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                              TaskRepository taskRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));
                    projectDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
                    projectDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

                    return projectDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Integer id) {

        return projectRepository.findById(id)
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));
                    projectDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
                    projectDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

                    return projectDTO;
                }).orElseThrow(() ->
                        new ResourceNotFoundException("Project with id:"+id+" doesn't exist",
                                                      ProjectController.URL_BASE + "/{project_id}")
                );
    }

    @Override
    public List<ProjectDTO> getProjectsByState(String state) {

        try {
            State stateSearch = State.valueOf(state.toUpperCase());

            return projectRepository.findByStateOfProject(stateSearch)
                    .stream()
                    .map(project -> {
                        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                        projectDTO.setProjectUrl(getProjectUrl(project.getId()));
                        projectDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
                        projectDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

                        return projectDTO;
                    }).collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Project state: "+state +" does not exist Database");
        }
    }

    @Override
    public List<ProjectDTO> getProjectsByYear(String year){

        // Temporal solution
        Date fromDate = null;
        Date tillDate = null;
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-01-01");
            tillDate = new SimpleDateFormat("yyy-MM-dd").parse(year + "-12-31");
        } catch (ParseException e) {
            throw new IllegalArgumentException("You have entered bad year: '"+year+"'");
        }

        return projectRepository.findByYear(fromDate, tillDate)
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));
                    projectDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
                    projectDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

                    return projectDTO;
                })
                .collect(Collectors.toList());

    }

    @Override
    public ProjectDTO getProjectByTitle(String title) {

        return projectRepository.findByTitle(title)
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));
                    projectDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
                    projectDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

                    return projectDTO;
                }).orElseThrow(() ->
                        new ResourceNotFoundException("Project with title:"+title+" doesn't exist",
                                                      ProjectController.URL_BASE + "/title/{title}")
                );
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {

        Project savedProject = projectRepository.save(projectMapper.projectDTOToProject(projectDTO));

        ProjectDTO savedDTO = projectMapper.projectToProjectDTO(savedProject);
        savedDTO.setProjectUrl(getProjectUrl(savedProject.getId()));
        savedDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
        savedDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

        return savedDTO;
    }

    @Override
    public ProjectDTO updateProject(Integer id, ProjectDTO projectDTO) {

        Project project = projectMapper.projectDTOToProject(projectDTO);
        project.setId(id);

        ProjectDTO savedProject = projectMapper.projectToProjectDTO(projectRepository.save(project));
        savedProject.setProjectUrl(getProjectUrl(id));
        savedProject.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
        savedProject.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

        return savedProject;
    }

    @Override
    public ProjectDTO patchProject(Integer id, ProjectDTO projectDTO) {

        return projectRepository.findById(id).map(project -> {

            if (projectDTO.getTitle() != null) {
                project.setTitle(projectDTO.getTitle());
            }

            if (projectDTO.getDescription() != null) {
                project.setDescription(projectDTO.getDescription());
            }

            if (projectDTO.getStateOfProject() != null) {
                project.setStateOfProject(projectDTO.getStateOfProject());
            }

            if (projectDTO.getStartDate() != null) {
                project.setStartDate(projectDTO.getStartDate());
            }

            if (projectDTO.getEndDate() != null) {
                project.setEndDate(projectDTO.getEndDate());
            }

            if (projectDTO.getCost() != null) {
                project.setCost(projectDTO.getCost());
            }

            ProjectDTO savedDTO = projectMapper.projectToProjectDTO(projectRepository.save(project));
            savedDTO.setProjectUrl(getProjectUrl(id));
            savedDTO.getTasks().forEach(task -> task.setTaskUrl(getTaskUrl(task.getId())));
            savedDTO.getEmployees().forEach(employee -> employee.setEmployeeUrl(getEmployeeUrl(employee.getId())));

            return savedDTO;
        }).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+id+" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}")
        );
    }

    @Override
    public void deleteProjectById(Integer id) {

        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+id+" doesn't exist",
                        ProjectController.URL_BASE + "/{project_id}"));

        projectRepository.delete(project);
    }

    @Override
    public ProjectDTO addEmployeeToProject(Integer projectId, Integer employeeId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/add_employee/{employeeId}")
        );

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/add_employee/{employeeId}")
        );

        project.getEmployees().add(employee);
        employee.getProjects().add(project);

        employeeRepository.save(employee);
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(projectRepository.save(project));
        projectDTO.setProjectUrl(getProjectUrl(projectId));
        projectDTO.getTasks().forEach(taskDTO -> taskDTO.setTaskUrl(getTaskUrl(taskDTO.getId())));
        projectDTO.getEmployees().forEach(employeeDTO -> employeeDTO.setEmployeeUrl(getEmployeeUrl(employeeDTO.getId())));

        return projectDTO;
    }

    @Override
    public ProjectDTO deleteEmployeeFromProject(Integer projectId, Integer employeeId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/delete_employee/{employeeId}")
        );

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Employee with id:"+ employeeId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/delete_employee/{employeeId}")
        );

        project.getEmployees().remove(employee);
        employee.getProjects().remove(project);

        employeeRepository.save(employee);
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(projectRepository.save(project));
        projectDTO.setProjectUrl(getProjectUrl(projectId));
        projectDTO.getTasks().forEach(taskDTO -> taskDTO.setTaskUrl(getTaskUrl(taskDTO.getId())));
        projectDTO.getEmployees().forEach(employeeDTO -> employeeDTO.setEmployeeUrl(getEmployeeUrl(employeeDTO.getId())));

        return projectDTO;
    }

    @Override
    public ProjectDTO deleteAllEmployeesFromProject(Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/clear_employees")
        );

        project.getEmployees().forEach(employee -> {
            employee.getProjects().remove(project);
            employeeRepository.save(employee);
        });

        project.setEmployees(new HashSet<>());
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(projectRepository.save(project));
        projectDTO.setProjectUrl(getProjectUrl(projectId));
        projectDTO.getTasks().forEach(taskDTO -> taskDTO.setTaskUrl(getTaskUrl(taskDTO.getId())));
        projectDTO.getEmployees().forEach(employeeDTO -> employeeDTO.setEmployeeUrl(getEmployeeUrl(employeeDTO.getId())));

        return projectDTO;
    }

    @Override
    public ProjectDTO deleteTaskFromProject(Integer projectId, Integer taskId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/delete_task/{taskId}")
        );

        for (Task task : project.getTasks()) {
            if (task.getId().equals(taskId)) {
                project.getTasks().remove(task);
                taskRepository.deleteById(task.getId());
                break;
            }
        }

        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
        projectDTO.setProjectUrl(getProjectUrl(projectId));
        projectDTO.getTasks().forEach(taskDTO -> taskDTO.setTaskUrl(getTaskUrl(taskDTO.getId())));
        projectDTO.getEmployees().forEach(employeeDTO -> employeeDTO.setEmployeeUrl(getEmployeeUrl(employeeDTO.getId())));

        return projectDTO;
    }

    @Override
    public ProjectDTO deleteAllTasksFromProject(Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/clear_tasks")
        );

        project.getTasks().forEach(task -> {
            project.getTasks().remove(task);
            taskRepository.deleteById(task.getId());
        });

        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
        projectDTO.setProjectUrl(getProjectUrl(projectId));
        projectDTO.getTasks().forEach(taskDTO -> taskDTO.setTaskUrl(getTaskUrl(taskDTO.getId())));
        projectDTO.getEmployees().forEach(employeeDTO -> employeeDTO.setEmployeeUrl(getEmployeeUrl(employeeDTO.getId())));

        return projectDTO;
    }

    private String getProjectUrl(Integer id) {
        return ProjectController.URL_BASE + "/" + id;
    }

    private String getTaskUrl(Integer id) { return TaskController.BASE_URL + "/" + id; }

    private String getEmployeeUrl(Integer id) { return EmployeeController.BASE_URL + "/" + id; }

}
