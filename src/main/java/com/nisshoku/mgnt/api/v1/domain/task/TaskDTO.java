package com.nisshoku.mgnt.api.v1.domain.task;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO extends TaskBaseDTO {

    private String projectUrl;
}
