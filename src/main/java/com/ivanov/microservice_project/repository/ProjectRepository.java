package com.ivanov.microservice_project.repository;

import com.ivanov.microservice_project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> getProjectById(Long projectId);
    // Здесь можно добавить кастомные запросы, если потребуется
}

