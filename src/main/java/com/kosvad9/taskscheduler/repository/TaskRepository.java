package com.kosvad9.taskscheduler.repository;

import com.kosvad9.taskscheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByCompleteStatusIsFalseAndUserId(Long userId);
    List<Task> findTasksByCompleteStatusIsTrueAndUserId(Long userId);
    Task getTaskByIdAndUserId(Long taskId, Long userId);
}
