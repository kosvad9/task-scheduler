package com.kosvad9.taskscheduler.service;

import com.kosvad9.taskscheduler.dto.TaskDto;
import com.kosvad9.taskscheduler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private List<TaskDto> getAllTasks(Long userId){
        return null;
    }


}
