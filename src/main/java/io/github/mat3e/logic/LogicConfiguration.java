package io.github.mat3e.logic;

import io.github.mat3e.TaskConfiguration;
import io.github.mat3e.model.ProjectRepository;
import io.github.mat3e.model.TaskGroupRepository;
import io.github.mat3e.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(ProjectRepository projectRepository
            , TaskGroupRepository taskGroupRepository
            , TaskGroupService taskGroupService
            , TaskConfiguration taskConfiguration
    ){
            return new ProjectService(projectRepository, taskGroupRepository, taskGroupService, taskConfiguration);
    }

    @Bean
    TaskGroupService taskGroupService(TaskGroupRepository repository, TaskRepository taskRepository){
        return new TaskGroupService(repository,taskRepository);
    }
}
