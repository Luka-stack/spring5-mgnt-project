package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.project.ProjectDTO;
import com.nisshoku.mgnt.api.v1.mappers.ProjectMapper;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.springframework.stereotype.Service;

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

                    return projectDTO;
                }).orElseThrow(RuntimeException::new);
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

                        return projectDTO;
                    }).collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new RuntimeException("Wrong State");
        }
    }

    @Override
    public List<ProjectDTO> getProjectsByYear(String year) {

        // Temporal solution
        Date fromDate = null;
        Date tillDate = null;
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-01-01");
            tillDate = new SimpleDateFormat("yyy-MM-dd").parse(year + "-12-31");
        } catch (ParseException e) {
            // TODO implement exception
            throw new RuntimeException("Bad Year");
        }

        return projectRepository.findByYear(fromDate, tillDate)
                .stream()
                .map(project -> {
                    ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
                    projectDTO.setProjectUrl(getProjectUrl(project.getId()));

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

                    return projectDTO;
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {

        Project savedProject = projectRepository.save(projectMapper.projectDTOToProject(projectDTO));

        ProjectDTO savedDTO = projectMapper.projectToProjectDTO(savedProject);
        savedDTO.setProjectUrl(getProjectUrl(savedProject.getId()));

        return savedDTO;
    }

    @Override
    public ProjectDTO updateProject(Integer id, ProjectDTO projectDTO) {

        Project project = projectMapper.projectDTOToProject(projectDTO);
        project.setId(id);

        ProjectDTO savedProject = projectMapper.projectToProjectDTO(projectRepository.save(project));
        savedProject.setProjectUrl(getProjectUrl(id));

        return savedProject;
    }

    @Override
    public ProjectDTO patchProject(Integer id, ProjectDTO projectDTO) {

        Project project = projectRepository.findById(id).orElseThrow(RuntimeException::new);

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

        return savedDTO;
    }

    @Override
    public void deleteProjectById(Integer id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void addEmployeeToProject(Integer projectId, Integer employeeId) {

        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(RuntimeException::new);

        project.getEmployees().add(employee);
        employee.getProjects().add(project);

        projectRepository.save(project);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeFromProject(Integer projectId, Integer employeeId) {

        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(RuntimeException::new);

        project.getEmployees().remove(employee);
        employee.getProjects().remove(project);

        projectRepository.save(project);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteAllEmployeesFromProject(Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);

        project.getEmployees().forEach(employee -> {
            employee.getProjects().remove(project);
            employeeRepository.save(employee);
        });

        project.setEmployees(new HashSet<>());
        projectRepository.save(project);
    }

    @Override
    public void deleteTaskFromProject(Integer projectId, Integer taskId) {

        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);

        for (Task task : project.getTasks()) {
            if (task.getId().equals(taskId)) {
                project.getTasks().remove(task);
                taskRepository.deleteById(task.getId());
                break;
            }
        }
    }

    @Override
    public void deleteAllTasksFromProject(Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);

        project.getTasks().forEach(task -> {
            project.getTasks().remove(task);
            taskRepository.deleteById(task.getId());
        });
    }

    private String getProjectUrl(Integer id) {
        return ProjectController.URL_BASE + "/" + id;
    }
}
