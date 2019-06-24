package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TaskController.BASE_URL)
public class TaskController {

    public static final String BASE_URL = "/api/v1/tasks";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getAllTasks() {
        return new TaskListDTO(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO getTaskById(@PathVariable Integer id) { return taskService.getTaskById(id); }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getTasksByState(@PathVariable String state) {
        return new TaskListDTO(taskService.getTasksByState(state));
    }

    @PostMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody TaskDTO task, @PathVariable Integer projectId) {
        return taskService.createTask(projectId, task);
    }

    @PostMapping("/add_tasks/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListDTO addListOfTasks(@RequestBody TaskListDTO tasks, @PathVariable Integer projectId) {
        return new TaskListDTO(taskService.addListOfTasks(tasks, projectId));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateTask(@RequestBody TaskDTO task, @PathVariable Integer id) {
        return taskService.updateTask(id, task);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO patchTask(@RequestBody TaskDTO task, @PathVariable Integer id) {
        return taskService.patchTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

}
