package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.api.v1.mappers.TaskMapper;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.repositories.ProjectRepository;
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
    private final String TITLE = "Some Lazy Title";

    private TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        taskService = new TaskServiceImpl(taskRepository, projectRepository, TaskMapper.INSTANCE);
    }

    @Test
    public void getAllTask() {

        Task task = new Task();
        task.setTitle(TITLE);
        task.setProject(new Project());
        task.setId(ID);

        Task task2 = new Task();
        task2.setTitle(TITLE + 2);
        task2.setProject(new Project());
        task2.setId(ID + 1);

        // given
        List<Task> tasks = Arrays.asList(task, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        // when
        List<TaskDTO> taskDTOList = taskService.getAllTasks();

        // then
        assertEquals(2, taskDTOList.size());
    }

    @Test
    public void getTaskById() {

        // given
        Task task = new Task();
        task.setTitle(TITLE);
        task.setProject(new Project());
        task.setId(ID);

        when(taskRepository.findById(ID)).thenReturn(java.util.Optional.of(task));

        // when
        TaskDTO taskDTO = taskService.getTaskById(ID);

        // then
        assertEquals(taskDTO.getStateOfTask(), task.getStateOfTask());
    }

    @Test
    public void getTasksByState() {

        // given
        Task task = new Task();
        task.setId(ID);
        task.setProject(new Project());
        task.setStateOfTask(State.DONE);

        Task task2 = new Task();
        task2.setId(ID + 1);
        task2.setProject(new Project());
        task2.setStateOfTask(State.DONE);

        List<Task> tasks = Arrays.asList(task, task2);

        when(taskRepository.findByStateOfTask(any())).thenReturn(tasks);

        // when
        List<TaskDTO> dtoList = taskService.getTasksByState("DONE");

        // then
        assertEquals(2, dtoList.size());
        assertEquals(State.DONE, dtoList.get(0).getStateOfTask());
        assertEquals(State.DONE, dtoList.get(1).getStateOfTask());
    }
}