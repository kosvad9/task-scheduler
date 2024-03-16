package com.kosvad9.taskscheduler.repository;

import com.kosvad9.taskscheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
