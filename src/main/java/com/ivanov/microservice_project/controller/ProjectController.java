package com.ivanov.microservice_project.controller;

import com.ivanov.microservice_project.entity.*;
import com.ivanov.microservice_project.exception.ResourceNotFoundException;
import com.ivanov.microservice_project.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    // Получение всех проектов
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // Создание нового проекта
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    // Обновление проекта
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(projectId, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }

    // Удаление проекта
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Map<String, Boolean>> deleteProject(@PathVariable Long projectId) {
        Map<String, Boolean> response = projectService.deleteProject(projectId);
        return ResponseEntity.ok(response);
    }

    // Получение всех задач проекта
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    // Получение всех комментариев проекта
    @GetMapping("/{projectId}/comments")
    public ResponseEntity<List<Comment>> getProjectComments(@PathVariable Long projectId) {
        List<Comment> comments = commentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }

    // Добавление комментария к проекту
    @PostMapping("/{projectId}/comments")
    public ResponseEntity<Comment> addProjectComment(@PathVariable Long projectId, @RequestBody Comment comment) {
        comment.setProject(projectService.getProjectById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + projectId)));
        Comment savedComment = commentService.saveComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // Обновление комментария
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment comment = commentService.updateComment(commentId, updatedComment);
        return ResponseEntity.ok(comment);
    }

    // Удаление комментария
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // Обновление срока завершения проекта
    @PutMapping("/{projectId}/deadline")
    public ResponseEntity<Project> updateProjectDeadline(
            @PathVariable Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime deadline) {
        Project updatedProject = projectService.updateProjectDeadline(projectId, deadline);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping(value = "/{projectId}/files", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload multiple Files")
    public ResponseEntity<String> uploadFile(@PathVariable Long projectId, @RequestPart MultipartFile file) {
        fileStorageService.storeFile(file, projectId);
        return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
    }

    @GetMapping("/{projectId}/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long projectId, @PathVariable String fileName) {
        // Загружаем файл как ресурс
        Resource resource = fileStorageService.loadFileAsResource(projectId, fileName);

        try {
            // Возвращаем содержимое файла в ответе
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            // В случае ошибки выбрасываем исключение
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }

    // Добавление пользователей к проекту
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Project> addUsersToProject(@PathVariable Long projectId,
                                                     @RequestBody List<Long> userIds) {
        List<User> users = userIds.stream()
                .map(userId -> userService.getUserById(userId))
                .collect(Collectors.toList());

        Project updatedProject = projectService.addUsersToProject(projectId, users);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/{id}/calculate-cost")
    public ResponseEntity<Project> calculateProjectCost(@PathVariable Long id) {
        Project project = projectService.calculateProjectCost(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

