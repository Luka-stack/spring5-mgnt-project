package com.nisshoku.mgnt.api.v1.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDTO {

    List<TaskBaseDTO> tasks;
}
