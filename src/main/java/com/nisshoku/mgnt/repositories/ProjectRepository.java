package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.api.v1.domain.Project.ProjectDTO;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByTitle(String title);

    List<Project> findByStateOfProject(State state);

    @Query("select a from Project a where a.startDate between :fromDate and :tillDate")
    List<Project> findByYear(Date fromDate, Date tillDate);
}
