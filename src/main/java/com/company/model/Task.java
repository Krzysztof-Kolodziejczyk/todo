package com.company.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private boolean done;
    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    @Embedded
    private Audit audit = new Audit();

    // Hibernate needs it
    public Task() {
    }

    public Task(String description, LocalDateTime deadline)
    {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group)
    {
        this.description = description;
        this.deadline = deadline;
        if(group != null){
            this.group = group;
        }
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source)
    {
        this.description = source.description;
        this.done = source.done;
        this.deadline = source.deadline;
        this.group = source.group;
        // do not have to actualise updatedOn, because preMerge method will do it.
    }

}
