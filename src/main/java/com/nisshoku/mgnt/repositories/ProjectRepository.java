package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
