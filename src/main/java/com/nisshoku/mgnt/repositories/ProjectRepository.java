package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
}
