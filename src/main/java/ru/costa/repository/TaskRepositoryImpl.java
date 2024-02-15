package ru.costa.repository;

import org.springframework.stereotype.Repository;
import ru.costa.entity.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final List<Task> tasks = new LinkedList<>(){{
        this.add(new Task("First item"));
        this.add(new Task("Second item"));
    }};
    @Override
    public List<Task> getAllTasks() {
        return this.tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID uuid) {
        return this.tasks.stream()
                .filter(task -> task.uuid().equals(uuid))
                .findFirst();
    }
}
