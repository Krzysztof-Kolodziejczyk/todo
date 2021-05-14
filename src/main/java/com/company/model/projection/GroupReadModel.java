package com.company.model.projection;

import com.company.model.TaskGroup;
import com.company.model.Task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    private String description;

    // deadline from the latest task in group
    private LocalDateTime deadline;

    Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source){
        this.id = source.getId();
        description = source.getDescription();

        source.getTasks().stream()
                .map(Task::getDeadline)
                .max(LocalDateTime::compareTo)
                .ifPresent(date -> deadline = date);

        tasks = source.getTasks().stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
