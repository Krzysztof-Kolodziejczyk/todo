package com.company.adapter;

import com.company.model.Task;
import com.company.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    @Override
    List<Task> findAllByDoneIsFalseAndDeadlineBeforeOrDeadlineIsNull(LocalDateTime dateTime);

    @Override
    List<Task> findAllByGroup_Id(Integer id);
}
