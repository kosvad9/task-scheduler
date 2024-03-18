package com.kosvad9.taskscheduler.controller;

import com.kosvad9.taskscheduler.dto.TaskDto;
import com.kosvad9.taskscheduler.exception.ValidException;
import com.kosvad9.taskscheduler.service.TaskService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getAllTasks(@AuthenticationPrincipal UserDetails userDetails){
        return taskService.getAllTasks(Long.parseLong(userDetails.getUsername()));
    }

    @GetMapping("/completed")
    public List<TaskDto> getCompletedTasks(@AuthenticationPrincipal UserDetails userDetails){
        return taskService.getCompletedTasks(Long.parseLong(userDetails.getUsername()));
    }

    @GetMapping("/uncompleted")
    public List<TaskDto> getUncompletedTasks(@AuthenticationPrincipal UserDetails userDetails){
        return taskService.getUncompletedTasks(Long.parseLong(userDetails.getUsername()));
    }

    @PostMapping
    public TaskDto createTask(@RequestBody TaskDto task, @AuthenticationPrincipal UserDetails userDetails){
        return taskService.createTask(Long.parseLong(userDetails.getUsername()), task);
    }

    @PutMapping
    public void editTask(@RequestBody @Validated TaskDto task, BindingResult bindingResult,
                         @AuthenticationPrincipal UserDetails userDetails){
        if (bindingResult.hasErrors()) throw new ValidException(bindingResult);
        taskService.editTask(Long.parseLong(userDetails.getUsername()), task);
    }

    @DeleteMapping
    public void deleteTask(@Validated @NotNull Long taskId, BindingResult bindingResult,
                           @AuthenticationPrincipal UserDetails userDetails){
        if (bindingResult.hasErrors()) throw new ValidException(bindingResult);
        taskService.deleteTask(Long.parseLong(userDetails.getUsername()), taskId);
    }

}
