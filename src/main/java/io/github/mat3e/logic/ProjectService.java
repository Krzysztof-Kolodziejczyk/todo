package io.github.mat3e.logic;

import io.github.mat3e.TaskConfiguration;
import io.github.mat3e.model.*;
import io.github.mat3e.model.projection.GroupReadModel;
import io.github.mat3e.model.projection.GroupTaskWriteModel;
import io.github.mat3e.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService taskGroupService;
    private TaskConfiguration taskConfiguration;

    public ProjectService(ProjectRepository repository, TaskGroupRepository groupRepository, TaskGroupService taskGroupService, TaskConfiguration taskConfiguration) {
        this.repository = repository;
        this.taskGroupRepository = groupRepository;
        this.taskGroupService = taskGroupService;
        this.taskConfiguration = taskConfiguration;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project save(Project source){
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if(!taskConfiguration.getTemplate().isAllowMultipleTasks() &&
            taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("only one undone group from project is allowed");
        }


        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();

                    targetGroup.setDescription(project.getDescription());

                    targetGroup.setTasks(project.getProjectSteps().stream()
                            .map(projectStep -> {
                                var task = new GroupTaskWriteModel();
                                task.setDescription(projectStep.getDescription());
                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                return task;
                            }).collect(Collectors.toSet())
                    );

                    return taskGroupService.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
