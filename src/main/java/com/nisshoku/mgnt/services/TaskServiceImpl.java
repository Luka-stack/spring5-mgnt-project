package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
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

    //TODO What with return

    @Override
    public List<TaskBaseDTO> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(taskMapper::taskToTaskBaseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskBaseDTO> getTasksByState(String state) {

        try {
            State taskState = State.valueOf(state.toUpperCase());

            return taskRepository.findByStateOfTask(taskState)
                .stream()
                .map(taskMapper::taskToTaskBaseDTO)
                .collect(Collectors.toList());
        }
        catch (IllegalArgumentException error) {
            throw new RuntimeException("Wrong State");
        }
    }

    @Override
    public List<TaskBaseDTO> addListOfTasks(TaskListDTO tasks, Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        TaskController.BASE_URL + "/add_tasks/{projectId}")
        );

        List<TaskBaseDTO> returnedTasks = new ArrayList<>();

        tasks.getTasks().forEach(taskBaseDTO -> {
            Task task = taskMapper.taskBaseDTOToTask(taskBaseDTO);
            project.getTasks().add(task);
            task.setProject(project);
            returnedTasks.add(taskMapper.taskToTaskBaseDTO(taskRepository.save(task)));
        });

        projectRepository.save(project);

        return returnedTasks;
    }

    @Override
    public TaskBaseDTO createTask(TaskBaseDTO task, Integer projectId) {

        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project with id:"+ projectId +" doesn't exist",
                        TaskController.BASE_URL + "/project/{projectId}")
        );
        Task taskToSave = taskMapper.taskBaseDTOToTask(task);

        project.getTasks().add(taskToSave);
        TaskBaseDTO savedTask = taskMapper.taskToTaskBaseDTO(taskRepository.save(taskToSave));

        return savedTask;
    }

    @Override
    public void deleteTaskById(Integer id) {

        taskRepository.deleteById(id);
    }

}
