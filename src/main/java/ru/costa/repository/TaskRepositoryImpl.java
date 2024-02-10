package ru.costa.repository;

import org.springframework.stereotype.Repository;
import ru.costa.entity.Task;

import java.util.LinkedList;
import java.util.List;

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
}
