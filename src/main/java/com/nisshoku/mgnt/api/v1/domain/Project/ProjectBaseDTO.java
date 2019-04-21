package com.nisshoku.mgnt.api.v1.domain.Project;

import com.nisshoku.mgnt.domain.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBaseDTO {

    private String title;
    private String description;
    private State stateOfProject;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double cost;
    private String projectUrl;
}
