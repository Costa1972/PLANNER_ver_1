package ru.costa.repository;

import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Repository;
import ru.costa.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TaskRepository {
    List<Task> getAllTasks();
    void save(Task task);

    Optional<Task> findById(UUID uuid);
}
