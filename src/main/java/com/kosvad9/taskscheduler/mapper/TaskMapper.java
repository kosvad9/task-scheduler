package com.kosvad9.taskscheduler.mapper;

import com.kosvad9.taskscheduler.dto.TaskDto;
import com.kosvad9.taskscheduler.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper implements Mapper<TaskDto, Task> {
    @Override
    public TaskDto mapToDto(Task entity) {
        return new TaskDto(entity.getId(),
                entity.getHeader(),
                entity.getText(),
                entity.getCompleteStatus(),
                entity.getCompleteTime());
    }

    @Override
    public Task mapToEntity(TaskDto dto) {
        return Task.builder()
                .id(dto.id())
                .header(dto.header())
                .text(dto.text())
                .completeStatus(dto.completeStatus())
                .completeTime(dto.completeStatus() ? LocalDateTime.now():null)
                .build();
    }
}
