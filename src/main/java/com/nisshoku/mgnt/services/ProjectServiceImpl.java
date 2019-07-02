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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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
                .map(this::setAllUrl)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Integer id) {

        return projectRepository.findById(id)
                .map(this::setAllUrl).orElseThrow(() ->
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
                    .map(this::setAllUrl).collect(Collectors.toList());
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
                .map(this::setAllUrl)
                .collect(Collectors.toList());

    }

    @Override
    public ProjectDTO getProjectByTitle(String title) {

        return projectRepository.findByTitle(title)
                .map(this::setAllUrl).orElseThrow(() ->
                        new ResourceNotFoundException("Project with title:"+title+" doesn't exist",
                                                      ProjectController.URL_BASE + "/title/{title}")
                );
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {

        Project savedProject = projectRepository.save(projectMapper.projectDTOToProject(projectDTO));
        return setAllUrl(savedProject);
    }

    @Override
    public ProjectDTO updateProject(Integer id, ProjectDTO projectDTO) {

        Project project = projectMapper.projectDTOToProject(projectDTO);
        project.setId(id);

        Project savedProject = projectRepository.save(project);
        return setAllUrl(savedProject);
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

            Project savedProject = projectRepository.save(project);
            return setAllUrl(savedProject);
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
        Project projectSaved = projectRepository.save(project);

        return setAllUrl(projectSaved);
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
        Project projectSaved = projectRepository.save(project);

        return setAllUrl(projectSaved);
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

        project.getEmployees().clear();
        Project projectSaved = projectRepository.save(project);

        return setAllUrl(projectSaved);
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

        return setAllUrl(project);
    }

    @Override
    public ProjectDTO deleteAllTasksFromProject(Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        ProjectController.URL_BASE + "/{projectId}/clear_tasks")
        );

        List<Task> taskList = new CopyOnWriteArrayList<>(project.getTasks());

        for (Task tk : taskList) {
            project.getTasks().remove(tk);
            taskRepository.delete(tk);
        }

        return setAllUrl(project);
    }

    private ProjectDTO setAllUrl(Project project) {

        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
        projectDTO.setProjectUrl(ProjectController.URL_BASE + "/" + project.getId());
        projectDTO.getTasks().forEach(task -> task.setTaskUrl(TaskController.BASE_URL + "/" + task.getId()));
        projectDTO.getEmployees().forEach(employee ->
                employee.setEmployeeUrl(EmployeeController.BASE_URL + "/" + employee.getId()));

        return projectDTO;
    }
}
