package com.nisshoku.mgnt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends BaseEntity {

    @NotBlank
    @Size(min=5, max=30)
    private String title;

    @Lob
    @NotBlank
    @Size(min=10, max=255)
    private String description;

 /*   @ManyToOne
    private Project project;*/
}
