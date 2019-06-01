package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskBaseDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TaskController.BASE_URL)
public class TaskController {

    public static final String BASE_URL = "/api/v1/tasks";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Future Development: Add Get Task By Project

    // TODO Add tests for exceptions

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getAllTasks() {
        return new TaskListDTO(taskService.getAllTasks());
    }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getTasksByState(@PathVariable String state) {
        return new TaskListDTO(taskService.getTasksByState(state));
    }

    @PutMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskBaseDTO createTask(@RequestBody TaskBaseDTO task, @PathVariable Integer projectId) {
        return taskService.createTask(task, projectId);
    }

    @PutMapping("/add_tasks/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListDTO addListOfTasks(@RequestBody TaskListDTO tasks, @PathVariable Integer projectId) {
        return new TaskListDTO(taskService.addListOfTasks(tasks, projectId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

}
