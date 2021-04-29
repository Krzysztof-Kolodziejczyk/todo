package io.github.mat3e.logic;

import io.github.mat3e.TaskConfiguration;
import io.github.mat3e.model.*;
import io.github.mat3e.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository groupRepository;
    private TaskConfiguration taskConfiguration;

    public ProjectService(ProjectRepository repository, TaskGroupRepository groupRepository, TaskConfiguration taskConfiguration) {
        this.repository = repository;
        this.groupRepository = groupRepository;
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
            groupRepository.existsByDoneIsFalseAndProject_Id(projectId)){
            throw new IllegalStateException("only one undone group from project is allowed");
        }

        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getProjectSteps().stream()
                            .map(projectStep -> new Task(
                                    projectStep.getDescription(),
                                    deadline.plusDays(projectStep.getDaysToDeadline()))
                            ).collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return groupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

        return new GroupReadModel(result);
    }
}
