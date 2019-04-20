package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
