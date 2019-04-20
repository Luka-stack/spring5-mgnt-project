package com.nisshoku.mgnt.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"projects"})
@Entity
@Table(name = "employees")
public class Employee extends Person {

    @Digits(integer = 9, fraction = 0)
    private String phoneNumber;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Language favoriteLanguage;

    @ManyToMany(mappedBy = "employees")
    private Set<Project> projects = new HashSet<>();
}
