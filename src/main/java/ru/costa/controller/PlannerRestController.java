package ru.costa.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.costa.entity.Task;
import ru.costa.repository.NewTaskPayload;
import ru.costa.repository.TaskRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class PlannerRestController {

    private final TaskRepository taskRepository;

    public PlannerRestController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public ResponseEntity<List<Task>> handleGetAllTasks() {
       return ResponseEntity.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .body(this.taskRepository.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> handleCreateNewTask(
            @RequestBody NewTaskPayload payload, UriComponentsBuilder uriComponentsBuilder) {
        Task task = new Task(payload.details());
        taskRepository.save(task);
        return ResponseEntity.created(uriComponentsBuilder
                .path("/api/tasks/{taskId}")
                .build(Map.of("taskId", task.uuid())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }
}