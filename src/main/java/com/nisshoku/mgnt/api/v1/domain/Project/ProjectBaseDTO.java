package com.nisshoku.mgnt.api.v1.domain.Project;

import com.nisshoku.mgnt.domain.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBaseDTO {

    private String title;
    private State stateOfProject;
    private String projectUrl;
}
