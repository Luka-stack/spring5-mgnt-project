package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;

import java.util.List;

public interface TaskService {

    List<TaskBaseDTO> getAllTasks();

    List<TaskBaseDTO> getTasksByState(String state);

    List<TaskBaseDTO> addListOfTasks(TaskListDTO tasks, Integer projectId);

    TaskBaseDTO createTask(TaskBaseDTO task, Integer projectId);

    void deleteTaskById(Integer id);
}
