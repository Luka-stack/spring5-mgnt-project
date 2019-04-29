package com.nisshoku.mgnt.repositories;

import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByStateOfTask(State state);

    List<Task> findByStateOfTaskIsNotLike(State state);
}
