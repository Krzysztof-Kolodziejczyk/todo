package io.github.mat3e.controller;

import io.github.mat3e.logic.TaskService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService taskService;

    public TaskController(TaskRepository repository, TaskService taskService) {
        this.repository = repository;
        this.taskService = taskService;
    }


    @GetMapping(params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("exposing all the tasks");
        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping()
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.warn("exposing all the tasks");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTask(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(repository.findByDone(state));
    }

    @GetMapping("/search/today")
    ResponseEntity<List<Task>> readTasksForToday(){
        return ResponseEntity.ok(repository.findAllByDoneIsFalseAndDeadlineIsNullOrDeadlineBefore(LocalDateTime.now()));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@RequestBody Task toUpdate, @PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.findById(id).ifPresent(task -> {
                task.updateFrom(toUpdate);
                repository.save(task);
            });
            logger.warn("update committed");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("update not committed");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task result = repository.save(task);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTaskById(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
            logger.warn("toggle committed");
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("toggle not committed");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    ResponseEntity<?> deleteAll() {
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}