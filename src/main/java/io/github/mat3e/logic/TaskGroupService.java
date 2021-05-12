package io.github.mat3e.logic;

import io.github.mat3e.model.TaskGroup;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import io.github.mat3e.model.projection.GroupReadModel;
import io.github.mat3e.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;


public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroupId(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all tasks first");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given Id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
