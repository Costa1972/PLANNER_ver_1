package ru.costa.controller;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.costa.entity.Task;
import ru.costa.error.ErrorsPresentation;
import ru.costa.repository.NewTaskPayload;
import ru.costa.repository.TaskRepository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class PlannerRestController {

    private final TaskRepository taskRepository;
    private final MessageSource messageSource;

    public PlannerRestController(TaskRepository taskRepository,
                                 MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Task>> handleGetAllTasks() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.taskRepository.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<?> handleCreateNewTask(
            @RequestBody NewTaskPayload payload,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale) {
        if (payload.details() == null || payload.details().isBlank()) {
            final var message = this.messageSource.getMessage("task.create.details.error.not_set",
                    new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(message)));
        } else {
            Task task = new Task(payload.details());
            taskRepository.save(task);
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/tasks/{taskId}")
                            .build(Map.of("taskId", task.uuid())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(task);
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<Task> handleFindTask(@PathVariable("id") UUID id) {
        return ResponseEntity.of(this.taskRepository.findById(id));
    }
}
