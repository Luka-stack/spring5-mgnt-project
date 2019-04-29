package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.mappers.TaskMapper;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskBaseDTO> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(taskMapper::taskToTaskBaseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskBaseDTO> getTasksByState(State state) {

        return taskRepository.findByStateOfTask(state)
                .stream()
                .map(taskMapper::taskToTaskBaseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskBaseDTO> getNotDoneTasks() {

        return taskRepository.findByStateOfTaskIsNotLike(State.DONE)
                .stream()
                .map(taskMapper::taskToTaskBaseDTO)
                .collect(Collectors.toList());
    }
}
