package com.nisshoku.mgnt.api.v1.domain.project;

import com.nisshoku.mgnt.domain.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBaseDTO {

    // TODO Think about adding ID to DTO for easy access in Controller
    private String title;
    private String description;
    private State stateOfProject;
    private Date startDate;
    private Date endDate;
    private Double cost;
    private String projectUrl;
}
