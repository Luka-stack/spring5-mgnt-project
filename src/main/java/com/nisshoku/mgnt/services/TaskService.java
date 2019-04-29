package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.domain.State;

import java.util.List;

public interface TaskService {

    List<TaskBaseDTO> getAllTasks();

    List<TaskBaseDTO> getTasksByState(State state);

    List<TaskBaseDTO> getNotDoneTasks();
}
