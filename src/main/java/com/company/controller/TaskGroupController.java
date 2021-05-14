package com.company.controller;

import com.company.logic.TaskGroupService;
import com.company.model.Task;
import com.company.model.TaskGroupRepository;
import com.company.model.TaskRepository;
import com.company.model.projection.GroupReadModel;
import com.company.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private final Logger logger = LoggerFactory.getLogger(TaskGroupRepository.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    public TaskGroupController(TaskGroupService taskGroupService, TaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        logger.warn("exposing all groups");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel group){
        GroupReadModel result = taskGroupService.createGroup(group);
        return ResponseEntity.created(URI.create("/" + result.getId()))
                .body(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTaskById(@PathVariable Integer id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
