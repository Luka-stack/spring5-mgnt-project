package com.nisshoku.mgnt.api.v1.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListDTO {

    List<TaskBaseDTO> tasks;
}
