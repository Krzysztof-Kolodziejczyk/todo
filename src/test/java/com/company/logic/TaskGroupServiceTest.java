package com.company.logic;


import com.company.model.TaskGroup;
import com.company.model.TaskGroupRepository;
import com.company.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName(value = "toggleGroup test existsByDoneIsFalseAndGroupId is true")
    void toggleGroup_test_existsByDoneIsFalseAndGroupId_is_true_throws_illegalArgumentException(){

        // given
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt()))
                .thenReturn(true);

        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));


        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName(value = "toggleGroup test existsByDoneIsFalseAndGroupId is false " +
            "and taskGroupRepository does not contain object of given id")
    void toggleGroup_test_taskGroupRepository_contains_no_objects_of_given_id(){
        // given
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt()))
                .thenReturn(false);
        // and
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id not found");
    }

    @Test
    @DisplayName(value = "toggleGroup test existsByDoneIsFalseAndGroupId is false " +
            "and taskGroupRepository contains object of given id")
    void toggleGroup_test_taskGroupRepository_contains_object_of_given_id(){
        // given
        TaskRepository mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroupId(anyInt()))
                .thenReturn(false);
        // and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        toTest.toggleGroup(0);

        // then
        assertThat(!group.isDone()).isEqualTo(beforeToggle);


    }

}