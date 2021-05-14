package com.company.logic;

import com.company.model.ProjectRepository;
import com.company.model.TaskGroupRepository;
import com.company.TaskConfiguration;
import com.company.model.TaskRepository;
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
