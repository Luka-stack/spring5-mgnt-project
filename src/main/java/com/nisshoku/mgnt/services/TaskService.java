package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByState(String state);

    List<TaskDTO> addListOfTasks(TaskListDTO tasks, Integer projectId);

    TaskDTO getTaskById(Integer id);

    TaskDTO createTask(Integer projectId, TaskDTO task);

    TaskDTO patchTask(Integer id, TaskDTO task);

    TaskDTO updateTask(Integer id, TaskDTO task);

    void deleteTaskById(Integer id);
}
