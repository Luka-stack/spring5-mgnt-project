package com.nisshoku.mgnt.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Project extends BaseEntity {

    @NotBlank
    @Size(min=5, max = 30)
    @Column(unique=true)
    private String title;

    @Lob
    @NotBlank
    @Size(min=10, max = 255)
    private String description;

    @NotNull
    private State stateOfProject;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;
    private Double cost;

    @ManyToMany
    @JoinTable(name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();
}
