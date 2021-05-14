package com.company.logic;

import com.company.model.TaskGroup;
import com.company.model.TaskGroupRepository;
import com.company.model.Project;
import com.company.model.TaskRepository;
import com.company.model.projection.GroupReadModel;
import com.company.model.projection.GroupWriteModel;

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
        return createGroup(source, null);
    }

    public GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
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
