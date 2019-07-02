package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.api.v1.mappers.TaskMapper;
import com.nisshoku.mgnt.controllers.v1.ProjectController;
import com.nisshoku.mgnt.controllers.v1.TaskController;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.exceptions.ResourceNotFoundException;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;


    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO getTaskById(Integer id) {

        return taskRepository.findById(id)
            .map(this::setAllUrl).orElseThrow(() ->
                    new ResourceNotFoundException("Task with id: " + id + " doesn't exist",
                            TaskController.BASE_URL+ "/{taskId}")
            );
    }

    @Override
    public List<TaskDTO> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::setAllUrl)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByState(String state) {

        try {
            State taskState = State.valueOf(state.toUpperCase());

            return taskRepository.findByStateOfTask(taskState)
                .stream()
                .map(this::setAllUrl)
                .collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Task State: "+state + "does not exist in Database");
        }
    }

    @Override
    public List<TaskDTO> addListOfTasks(TaskListDTO listDTO, Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        TaskController.BASE_URL + "/add_tasks/{projectId}")
        );

        List<TaskDTO> returnedTasks = new ArrayList<>();

        listDTO.getTasks().forEach(taskDTO -> {
            Task task = taskMapper.taskBaseDTOToTask(taskDTO);
            project.getTasks().add(task);
            task.setProject(project);
            Task savedTask = taskRepository.save(task);
            returnedTasks.add(setAllUrl(savedTask));
        });

        projectRepository.save(project);

        return returnedTasks;
    }

    @Override
    public TaskDTO createTask(Integer projectId, TaskDTO task) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        TaskController.BASE_URL + "/project/{projectId}")
        );
        Task taskToSave = taskMapper.taskBaseDTOToTask(task);

        project.getTasks().add(taskToSave);
        taskToSave.setProject(project);

        Task savedTask = taskRepository.save(taskToSave);

        return setAllUrl(savedTask);
    }

    @Override
    public TaskDTO updateTask(Integer id, TaskDTO taskDTO) {

       Task taskToUpdate = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task with id: " + id + " doesn't exist",
                        TaskController.BASE_URL+ "/{taskId}")
        );

        taskToUpdate.setTitle(taskDTO.getTitle());
        taskToUpdate.setDescription(taskDTO.getDescription());
        taskToUpdate.setStateOfTask(taskDTO.getStateOfTask());

        Task savedTask = taskRepository.save(taskToUpdate);

        return setAllUrl(savedTask);
    }

    @Override
    public TaskDTO patchTask(Integer id, TaskDTO taskDTO) {

        return taskRepository.findById(id).map(task -> {

            if (taskDTO.getTitle() != null)
                task.setTitle(taskDTO.getTitle());

            if (taskDTO.getDescription() != null)
                task.setDescription(taskDTO.getDescription());

            if (taskDTO.getStateOfTask() != null)
                task.setStateOfTask(taskDTO.getStateOfTask());

            return setAllUrl(task);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Task with id: " + id + " doesn't exist",
                        TaskController.BASE_URL+ "/{taskId}")
        );
    }

    @Override
    public void deleteTaskById(Integer id) {

        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task with id: " + id + " doesn't exist",
                        TaskController.BASE_URL+ "/{taskId}"));

        taskRepository.delete(task);
    }

    private TaskDTO setAllUrl(Task task) {

        TaskDTO dto = taskMapper.taskToTaskDTO(task);
        dto.setTaskUrl(TaskController.BASE_URL + "/" + task.getId());
        dto.setProjectUrl(ProjectController.URL_BASE + "/" + task.getProject().getId());

        return dto;
    }
}
