package com.kosvad9.taskscheduler.controller;

import com.kosvad9.taskscheduler.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.DeclareError;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    @GetMapping
    public List<TaskDto> getTasks(@AuthenticationPrincipal UserDetails userDetails){
        return null;
    }

    @PostMapping
    public void createTask(@RequestBody TaskDto task, @AuthenticationPrincipal UserDetails userDetails){

    }

    @PutMapping
    public void editTask(@RequestBody @Validated TaskDto task, @AuthenticationPrincipal UserDetails userDetails){

    }

    @DeleteMapping
    public void deleteTask(@RequestBody @Validated TaskDto task, @AuthenticationPrincipal UserDetails userDetails){

    }

}
