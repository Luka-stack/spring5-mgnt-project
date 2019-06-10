package com.nisshoku.mgnt.api.v1.domain.task;

import com.nisshoku.mgnt.domain.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskBaseDTO {

    // TODO Think about adding ID to DTO for easy access in Controller
    private String title;
    private String description;
    private State stateOfTask;
}
