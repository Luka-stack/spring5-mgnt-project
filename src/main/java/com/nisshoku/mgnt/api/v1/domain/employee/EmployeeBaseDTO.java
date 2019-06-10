package com.nisshoku.mgnt.api.v1.domain.employee;

import com.nisshoku.mgnt.domain.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBaseDTO {

    // TODO Think about adding ID to DTO for easy access in Controller

    private String firstName;
    private String lastName;
    private String email;
    private Language favoriteLanguage;
    private String phoneNumber;
    private String employeeUrl;
}
