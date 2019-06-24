package com.nisshoku.mgnt.services;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
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

        TaskDTO firstTask = new TaskDTO();
        firstTask.setTitle(TITLE);
        firstTask.setDescription(DESCRIPTION);
        firstTask.setStateOfTask(TASK_STATE);

        TaskDTO secondTask = new TaskDTO();
        secondTask.setTitle(TITLE + "Two");
        secondTask.setDescription(DESCRIPTION + "Two");
        secondTask.setStateOfTask(TASK_STATE);

        TaskListDTO taskToSave = new TaskListDTO(Arrays.asList(firstTask, secondTask));

        Project project = getValidProject();
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

        List<Task> taskList = taskRepository.findAll();
        assertNotNull(taskList);

        int beforeTaskSize = taskList.size();

        taskList = taskRepository.findAll();
        assertNotNull(taskList);

        assertEquals(beforeTaskSize, taskList.size());
    }

    @Test
    public void createTask() {

        Project project = getValidProject();
        assertNotNull(project);
        int beforeProjectsTasks = project.getTasks().size();

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);
        taskDTO.setDescription(DESCRIPTION);
        taskDTO.setStateOfTask(TASK_STATE);

        TaskDTO savedDTO = taskService.createTask(project.getId(), taskDTO);

        assertEquals(savedDTO.getTitle(), taskDTO.getTitle());
        assertEquals(beforeProjectsTasks + 1, project.getTasks().size());
    }

    @Test
    public void updateTask() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);
        taskDTO.setDescription(DESCRIPTION);
        taskDTO.setStateOfTask(TASK_STATE);

        Task taskDB = getValidTask();
        assertNotNull(taskDB);

        taskService.updateTask(taskDB.getId(), taskDTO);

        assertEquals(TITLE, taskDB.getTitle());
    }

    @Test
    public void patchTask() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(TITLE);

        Task taskDB = getValidTask();
        assertNotNull(taskDB);

        String descDB = taskDB.getDescription();

        taskService.patchTask(taskDB.getId(), taskDTO);

        assertEquals(TITLE, taskDB.getTitle());
        assertEquals(descDB, taskDB.getDescription());
    }

    private Project getValidProject() {

        List<Project> projects = projectRepository.findAll();

        return projects.get(0);
    }

    private Task getValidTask() {

        List<Task> tasks = taskRepository.findAll();

        return tasks.get(0);
    }
}
