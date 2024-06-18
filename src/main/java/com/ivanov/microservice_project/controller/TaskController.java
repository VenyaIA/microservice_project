package com.ivanov.microservice_project.controller;

import com.ivanov.microservice_project.entity.Comment;
import com.ivanov.microservice_project.entity.Task;
import com.ivanov.microservice_project.entity.enums.Priority;
import com.ivanov.microservice_project.entity.enums.TaskStatus;
import com.ivanov.microservice_project.exception.ResourceNotFoundException;
import com.ivanov.microservice_project.service.CommentService;
import com.ivanov.microservice_project.service.ProjectService;
import com.ivanov.microservice_project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    // получение задачи
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + taskId));
        return ResponseEntity.ok(task);
    }

    // Добавление задачи к проекту
    @PostMapping("/{projectId}")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long projectId, @RequestBody Task task) {
        task.setProject(projectService.getProjectById(projectId).orElse(null));
        Task savedTask = taskService.saveTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    // Обновление задачи
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(taskId, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    // Удаление задачи
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Map<String, Boolean>> deleteTask(@PathVariable Long taskId) {
        Map<String, Boolean> response = taskService.deleteTask(taskId);
        return ResponseEntity.ok(response);
    }

    // Получение завершенных задач проекта
    @GetMapping("/{projectId}/completed")
    public ResponseEntity<List<Task>> getCompletedTasksByProject(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getCompletedTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    // Получение задач по статусу для указанного проекта
    @GetMapping("/{projectId}/{status}")
    public ResponseEntity<List<Task>> getTasksByProjectAndStatus(
            @PathVariable Long projectId,
            @PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByProjectIdAndStatus(projectId, status);
        return ResponseEntity.ok(tasks);
    }

    // Получение всех комментариев задачи
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getTaskComments(@PathVariable Long taskId) {
        List<Comment> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    // Добавление комментария к задаче
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Comment> addTaskComment(@PathVariable Long taskId, @RequestBody Comment comment) {
        comment.setTask(taskService.getTaskById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + taskId)));
        Comment savedComment = commentService.saveComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // Обновление приоритета задачи
    @PutMapping("/{taskId}/priority")
    public ResponseEntity<Task> updateTaskPriority(
            @PathVariable Long taskId,
            @RequestParam Priority priority) {
        Task updatedTask = taskService.updateTaskPriority(taskId, priority);
        return ResponseEntity.ok(updatedTask);
    }

    // Обновление срока выполнения задачи
    @PutMapping("/{taskId}/due-date")
    public ResponseEntity<Task> updateTaskDueDate(
            @PathVariable Long taskId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        Task updatedTask = taskService.updateTaskDueDate(taskId, dueDate);
        return ResponseEntity.ok(updatedTask);
    }


}
