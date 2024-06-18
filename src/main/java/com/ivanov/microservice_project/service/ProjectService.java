package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.entity.Project;
import com.ivanov.microservice_project.entity.Task;
import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.exception.ResourceNotFoundException;
import com.ivanov.microservice_project.repository.ProjectRepository;
import com.ivanov.microservice_project.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, Project projectDetails) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + projectId));
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setDeadline(projectDetails.getDeadline());
        return projectRepository.save(project);
    }

    public Map<String, Boolean> deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + projectId));
        projectRepository.delete(project);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public Optional<Project> getProjectById(Long projectId) {
        return projectRepository.getProjectById(projectId);
    }

    public Project updateProjectDeadline(Long projectId, LocalDateTime deadline) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
        project.setDeadline(deadline);
        return projectRepository.save(project);
    }

    public List<User> getProjectUsers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + projectId));

        // Предположим, что в проекте есть коллекция пользователей, участвующих в нем
        return project.getUsers();
    }

    public Project addUsersToProject(Long projectId, List<User> users) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for this id :: " + projectId));

        // Добавляем пользователей к проекту
        project.getUsers().addAll(users);
        return projectRepository.save(project);
    }

    public Project calculateProjectCost(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            List<Task> tasks = taskRepository.findByProjectId(projectId);
            double totalCost = 0;
            for (Task task : tasks) {
                totalCost += task.getCost();
            }
            project.setTotalCost(totalCost);
            return projectRepository.save(project);
        }
        return null;
    }

//    public Project updateProjectBudget(Long id, double budget) {
//        Project project = projectRepository.findById(id).orElse(null);
//        if (project != null) {
//            project.setBudget(budget);
//            return projectRepository.save(project);
//        }
//        return null;
//    }
}

