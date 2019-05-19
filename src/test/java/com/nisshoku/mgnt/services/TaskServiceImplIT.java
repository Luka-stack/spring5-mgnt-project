package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.api.v1.mappers.TaskMapper;
import com.nisshoku.mgnt.bootstrap.DataLoader;
import com.nisshoku.mgnt.domain.Project;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.domain.Task;
import com.nisshoku.mgnt.repositories.EmployeeRepository;
import com.nisshoku.mgnt.repositories.ProjectRepository;
import com.nisshoku.mgnt.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskServiceImplIT {

    private final String TITLE = "Test Title for Task";
    private final String DESCRIPTION = "Test Description for Task";
    private final State TASK_STATE = State.BUG;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    private TaskService taskService;

    @Before
    public void setUp() throws Exception {
        DataLoader dataLoader = new DataLoader(employeeRepository, projectRepository, taskRepository);
        dataLoader.run();

        taskService = new TaskServiceImpl(taskRepository, projectRepository, TaskMapper.INSTANCE);
    }

    @Test
    public void addListOfTasks() {

        TaskBaseDTO firstTask = new TaskBaseDTO();
        firstTask.setTitle(TITLE);
        firstTask.setDescription(DESCRIPTION);
        firstTask.setStateOfTask(TASK_STATE);

        TaskBaseDTO secondTask = new TaskBaseDTO();
        secondTask.setTitle(TITLE + "Two");
        secondTask.setDescription(DESCRIPTION + "Two");
        secondTask.setStateOfTask(TASK_STATE);

        TaskListDTO taskToSave = new TaskListDTO(Arrays.asList(firstTask, secondTask));

        Project project = projectRepository.findById(1).get();
        assertNotNull(project);
        int beforeProjectsTasks = project.getTasks().size();
        int beforeTaskSize = taskRepository.findAll().size();

        taskService.addListOfTasks(taskToSave, project.getId());

        int afterTaskSize = taskRepository.findAll().size();

        assertEquals(beforeTaskSize + 2, afterTaskSize);
        assertEquals(beforeProjectsTasks + 2, project.getTasks().size());
    }

    @Test
    public void deleteTask() {

        List<Task> beforeDelete = taskRepository.findAll();

        taskService.deleteTaskById(beforeDelete.get(0).getId());

        List<Task> afterDelete = taskRepository.findAll();

        assertEquals(afterDelete.size(), beforeDelete.size()-1);
    }

    @Test
    public void createTask() {

        Project project = getValidProject();
        assertNotNull(project);
        int beforeProjectsTasks = project.getTasks().size();

        TaskBaseDTO taskDTO = new TaskBaseDTO();
        taskDTO.setTitle(TITLE);
        taskDTO.setDescription(DESCRIPTION);
        taskDTO.setStateOfTask(TASK_STATE);

        TaskBaseDTO savedDTO = taskService.createTask(taskDTO, project.getId());

        assertEquals(savedDTO.getTitle(), taskDTO.getTitle());
        assertEquals(beforeProjectsTasks + 1, project.getTasks().size());
    }

    private Project getValidProject() {

        List<Project> projects = projectRepository.findAll();

        return projects.get(0);
    }
}
