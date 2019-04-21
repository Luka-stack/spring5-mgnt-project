package com.nisshoku.mgnt.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Project extends BaseEntity {

    @NotBlank
    @Size(min=5, max = 30)
    private String title;

    @Lob
    @NotBlank
    @Size(min=10, max = 255)
    private String description;

    @NotNull
    private State stateOfProject;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
    private Double cost;

    @ManyToMany
    @JoinTable(name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees = new HashSet<>();

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();*/
}
