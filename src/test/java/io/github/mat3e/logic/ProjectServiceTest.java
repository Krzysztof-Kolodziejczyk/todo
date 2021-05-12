package io.github.mat3e.logic;

import io.github.mat3e.TaskConfiguration;
import io.github.mat3e.model.*;
import io.github.mat3e.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw illegalStateException when configured to " +
            "allow just 1 group and the other undone group exist")
    void createGroup_noMultipleGroupsConfig_and_undoneGroupExist_throwsIllegalSateException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfiguration mockConfig = ConfigurationReturning(false);

        // system under test
        var toTest = new ProjectService(null,mockGroupRepository, null, mockConfig);

        // when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone group");

    }

    @Test
    @DisplayName("should throw illegalArgumentException when configured ok and no project for given id")
    void createGroup_configurationOk_and_noProjects_throwsIllegalArgumentException() {
        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskConfiguration mockConfig = ConfigurationReturning(true);

        // system under test
        var toTest = new ProjectService(mockRepository,null, null, mockConfig);

        // when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should throw illegalArgumentException configured to allow just 1 group and no groups and project for given id")
    void createGroup_noMultipleGroupsConfig_and_noUndoneGroupExist_and_noProjects_throwsIllegalArgumentException() {
        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        //and
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskConfiguration mockConfig = ConfigurationReturning(true);

        // system under test
        var toTest = new ProjectService(mockRepository,null, null, mockConfig);

        // when

        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){
        // given
        var today = LocalDate.now().atStartOfDay();
        // and
        var mockRepository = mock(ProjectRepository.class);
        var project = projectWith("bar", Set.of(-1,-2));

        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        // and
        TaskConfiguration mockConfig = ConfigurationReturning(true);

        // and
        InMemoryGroupRepository inMemoryRepository = inMemoryGroupRepository();

        var serviceWithInMemRepo = dummyGroupService(inMemoryRepository);

        int countBeforeCall = inMemoryRepository.count();

        // system under test
        var toTest = new ProjectService(mockRepository,inMemoryRepository, serviceWithInMemRepo, mockConfig);

        // when
        GroupReadModel result = toTest.createGroup(1,today);

        // then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));

        assertThat(countBeforeCall + 1).isEqualTo(inMemoryRepository.count());

    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryRepository) {
        return new TaskGroupService(inMemoryRepository, null);
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        var steps = daysToDeadline.stream().map(days -> {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }


    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfiguration ConfigurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfiguration.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);

        var mockConfig = mock(TaskConfiguration.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);

        return mockConfig;
    }


    private InMemoryGroupRepository inMemoryGroupRepository(){
        return new InMemoryGroupRepository();
    }


    private static class InMemoryGroupRepository implements TaskGroupRepository{

            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            public int count(){
                return map.values().size();
            }

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if(entity.getId() == 0){
                    try {
                        var field = TaskGroup.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    }catch (NoSuchFieldException | IllegalAccessException ex){
                        throw new RuntimeException(ex);
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(TaskGroup::isDone)
                        .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
            }

    }


}