package ru.costa.repository;

import org.springframework.stereotype.Repository;
import ru.costa.entity.Task;

import java.util.List;


public interface TaskRepository {
    List<Task> getAllTasks();
    void save(Task task);
}
