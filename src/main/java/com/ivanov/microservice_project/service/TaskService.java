package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.entity.Task;
import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.entity.enums.Priority;
import com.ivanov.microservice_project.entity.enums.TaskStatus;
import com.ivanov.microservice_project.exception.ResourceNotFoundException;
import com.ivanov.microservice_project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task updateTask(Long taskId, Task taskDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + taskId));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStartDate(taskDetails.getStartDate());
        task.setEndDate(taskDetails.getEndDate());
        task.setCompleted(taskDetails.isCompleted());
        task.setPriority(taskDetails.getPriority());
        task.setStatus(taskDetails.getStatus());
        task.setMaterials(taskDetails.getMaterials());
        calculateTaskCost(task);
        return taskRepository.save(task);
    }

    public Map<String, Boolean> deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + taskId));
        taskRepository.delete(task);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public List<Task> getCompletedTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectIdAndCompleted(projectId, true);
    }

    public List<Task> getTasksByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.getTaskById(taskId);
    }

    public Task updateTaskPriority(Long taskId, Priority priority) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setPriority(priority);
        return taskRepository.save(task);
    }

    public Task updateTaskDueDate(Long taskId, LocalDate dueDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setEndDate(dueDate);
        return taskRepository.save(task);
    }

    public Task addUserToTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for this id :: " + taskId));

        // Добавляем пользователя к задаче
        task.setAssignedUser(user);
        return taskRepository.save(task);
    }

    private void calculateTaskCost(Task task) {
        User user = task.getAssignedUser();
        if (user != null) {
            long durationInHours = java.time.Duration.between(task.getStartDate().atStartOfDay(),
                    task.getEndDate().atStartOfDay()).toHours();
            double laborCost = durationInHours * user.getHourlyRate();
            double materialCost = task.calculateMaterialCost();
            task.setCost(laborCost + materialCost);
        }
    }
}