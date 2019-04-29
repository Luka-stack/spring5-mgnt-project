package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.mappers.TaskMapper;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaskServiceImplTest {

    private final Integer ID = 1;

    private TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        taskService = new TaskServiceImpl(taskRepository, TaskMapper.INSTANCE);
    }

    @Test
    public void getAllTask() {

        // given
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(taskRepository.findAll()).thenReturn(tasks);

        // when
        List<TaskBaseDTO> taskDTOList = taskService.getAllTasks();

        // then
        assertEquals(2, taskDTOList.size());
    }

    @Test
    public void getTasksByState() {

        // given
        Task task = new Task();
        task.setId(ID);
        task.setStateOfTask(State.DONE);

        Task task2 = new Task();
        task2.setId(ID);
        task2.setStateOfTask(State.DONE);

        List<Task> tasks = Arrays.asList(task, task2);

        when(taskRepository.findByStateOfTask(any())).thenReturn(tasks);

        // when
        List<TaskBaseDTO> dtoList = taskService.getTasksByState(State.DONE);

        // then
        assertEquals(2, dtoList.size());
        assertEquals(State.DONE, dtoList.get(0).getStateOfTask());
        assertEquals(State.DONE, dtoList.get(1).getStateOfTask());
    }

    @Test
    public void getNotDoneTasks() {

        // given
        Task task = new Task();
        task.setId(ID);
        task.setStateOfTask(State.IN_PROGRESS);

        Task task2 = new Task();
        task2.setId(ID);
        task2.setStateOfTask(State.BUG);

        List<Task> tasks = Arrays.asList(task, task2);

        when(taskRepository.findByStateOfTaskIsNotLike(any())).thenReturn(tasks);

        // when
        List<TaskBaseDTO> dtoList = taskService.getNotDoneTasks();

        // then
        assertEquals(2, dtoList.size());

    }
}