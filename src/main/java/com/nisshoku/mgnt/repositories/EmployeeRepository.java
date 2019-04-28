package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.domain.Employee;
import com.nisshoku.mgnt.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFavoriteLanguage(Language favoriteLanguage);

    List<Employee> findByLastName(String lastName);
}
