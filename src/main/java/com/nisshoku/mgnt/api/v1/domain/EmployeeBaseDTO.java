package com.nisshoku.mgnt.api.v1.domain;

import com.nisshoku.mgnt.domain.Language;
import com.nisshoku.mgnt.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBaseDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Language favoriteLanguage;
}
