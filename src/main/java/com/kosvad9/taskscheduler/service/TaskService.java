package com.kosvad9.taskscheduler.service;

import com.kosvad9.core.TaskDetail;
import com.kosvad9.core.TaskReport;
import com.kosvad9.taskscheduler.dto.TaskDto;
import com.kosvad9.taskscheduler.entity.Task;
import com.kosvad9.taskscheduler.entity.User;
import com.kosvad9.taskscheduler.mapper.Mapper;
import com.kosvad9.taskscheduler.repository.TaskRepository;
import com.kosvad9.taskscheduler.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Mapper<TaskDto, Task> dtoTaskMapper;

    public List<TaskDto> getAllTasks(Long userId){
        List<Task> tasks =  userRepository.findUserById(userId).map(User::getTasks)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return tasks.stream().map(dtoTaskMapper::mapToDto).toList();
    }

    public List<TaskDto> getCompletedTasks(Long userId){
        return taskRepository.findTasksByCompleteStatusIsTrueAndUserId(userId).stream()
                .map(dtoTaskMapper::mapToDto).toList();
    }

    public List<TaskDto> getUncompletedTasks(Long userId){
        return taskRepository.findTasksByCompleteStatusIsFalseAndUserId(userId).stream()
                .map(dtoTaskMapper::mapToDto).toList();
    }

    @Transactional
    public TaskDto createTask(Long userId, TaskDto taskDto){
        User user = userRepository.getReferenceById(userId);
        Task task = dtoTaskMapper.mapToEntity(taskDto);
        task.setUser(user);
        taskRepository.saveAndFlush(task);
        return dtoTaskMapper.mapToDto(task);
    }

    @Transactional
    public void editTask(Long userId, TaskDto taskDto){
        Task task = taskRepository.getTaskByIdAndUserId(taskDto.id(), userId);
        task.setHeader(taskDto.header());
        task.setText(taskDto.text());
        if (task.getCompleteStatus() != taskDto.completeStatus()){
            task.setCompleteStatus(taskDto.completeStatus());
            task.setCompleteTime(taskDto.completeStatus() ? LocalDateTime.now():null);
        }
        taskRepository.saveAndFlush(task);
    }

    @Transactional
    public void deleteTask(Long userId, Long taskId){
        Task task = taskRepository.getTaskByIdAndUserId(taskId, userId);
        taskRepository.delete(task);
    }

    public List<TaskReport> getTasksForReport(LocalDateTime from, LocalDateTime to){
        List<Task> tasks = taskRepository.findTasksByCompleteStatusIsFalseOrCompleteTimeBetween(from, to);
        Map<String, List<Task>> map = tasks.stream()
                .collect(Collectors.groupingBy(task -> task.getUser().getLogin()));
        return map.entrySet().stream()
                .map(entry -> {
                        List<TaskDetail> list = entry.getValue().stream()
                                .map(task -> new TaskDetail(task.getHeader(),
                                                                    task.getCompleteStatus(),
                                                                    task.getCompleteTime()))
                                .toList();
                        return new TaskReport(entry.getKey(), list);
                    })
                .toList();
    }
}
