package com.nisshoku.mgnt.api.v1.domain;

import com.nisshoku.mgnt.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private String title;
    private String description;
    private Project project;
}
