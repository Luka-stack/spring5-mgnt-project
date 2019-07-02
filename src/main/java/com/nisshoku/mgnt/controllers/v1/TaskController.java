package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskDTO;
import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TaskController.BASE_URL)
@Api(description = "Operations on 'Task' entity requires 'admin' or 'employee' role")
public class TaskController {

    public static final String BASE_URL = "/api/v1/tasks";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return All Tasks", authorizations = {@Authorization(value = "basic_auth")})
    public TaskListDTO getAllTasks() {
        return new TaskListDTO(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Task With Given ID", authorizations = {@Authorization(value = "basic_auth")})
    public TaskDTO getTaskById(@PathVariable Integer id) { return taskService.getTaskById(id); }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Return Task With Given State", authorizations = {@Authorization(value = "basic_auth")})
    public TaskListDTO getTasksByState(@PathVariable String state) {
        return new TaskListDTO(taskService.getTasksByState(state));
    }

    @PostMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create New Task", notes = "Require project (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public TaskDTO createTask(@RequestBody TaskDTO task, @PathVariable Integer projectId) {
        return taskService.createTask(projectId, task);
    }

    @PostMapping("/add_tasks/{projectId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create More than one Task", notes = "Require project (id)",
            authorizations = {@Authorization(value = "basic_auth")})
    public TaskListDTO addListOfTasks(@RequestBody TaskListDTO tasks, @PathVariable Integer projectId) {
        return new TaskListDTO(taskService.addListOfTasks(tasks, projectId));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update Task With Given ID", notes = "Doesn't create Task if ID is wrong",
            authorizations = {@Authorization(value = "basic_auth")})
    public TaskDTO updateTask(@RequestBody TaskDTO task, @PathVariable Integer id) {
        return taskService.updateTask(id, task);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Patch Task With Given ID", authorizations = {@Authorization(value = "basic_auth")})
    public TaskDTO patchTask(@RequestBody TaskDTO task, @PathVariable Integer id) {
        return taskService.patchTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete Task With Given ID", authorizations = {@Authorization(value = "basic_auth")})
    private void deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

}
