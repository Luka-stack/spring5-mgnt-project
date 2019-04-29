package com.nisshoku.mgnt.controllers.v1;

import com.nisshoku.mgnt.api.v1.domain.task.TaskListDTO;
import com.nisshoku.mgnt.domain.State;
import com.nisshoku.mgnt.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TaskController.BASE_URL)
public class TaskController {

    static final String BASE_URL = "/api/v1/tasks";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getAllTasks() {
        return new TaskListDTO(taskService.getAllTasks());
    }

    @GetMapping("/state/{state}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getTasksByState(@PathVariable State state) {
        return new TaskListDTO(taskService.getTasksByState(state));
    }

    @GetMapping("/notdone")
    @ResponseStatus(HttpStatus.OK)
    public TaskListDTO getNotDoneTasks() {
        return new TaskListDTO(taskService.getNotDoneTasks());
    }
}
