package com.nisshoku.mgnt.api.v1.mappers;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskBaseDTO taskToTaskBaseDTO(Task task);

    TaskDTO taskToTaskDTO(Task task);

    Task taskBaseDTOToTask(TaskBaseDTO task);
}