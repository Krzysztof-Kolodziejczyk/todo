package io.github.mat3e.controller;

import io.github.mat3e.model.Task;
import io.github.mat3e.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks()
    {
        logger.warn("exposing all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page)
    {
        logger.warn("exposing all the tasks");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@RequestBody Task toUpdate, @PathVariable Integer id)
    {
        if(repository.existsById(id))
        {
            repository.findById(id).ifPresent(task -> {
                task.updateFrom(toUpdate);
                repository.save(task);
            });
            logger.warn("update committed");
            return ResponseEntity.noContent().build();
        }
        else
        {
            logger.warn("update not committed");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody Task task)
    {
        Task result = repository.save(task);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable Integer id)
    {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTaskById(@PathVariable Integer id)
    {
        if(repository.existsById(id))
        {
            repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
            logger.warn("toggle committed");
            return ResponseEntity.noContent().build();
        }
        else
        {
            logger.warn("toggle not committed");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/tasks")
    ResponseEntity<?> deleteAll()
    {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }


};