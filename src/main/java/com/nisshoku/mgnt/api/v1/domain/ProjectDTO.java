package com.nisshoku.mgnt.api.v1.domain;

import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double cost;
    private Set<Employee> employees = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();

}
