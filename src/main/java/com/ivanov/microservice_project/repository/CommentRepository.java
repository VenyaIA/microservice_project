package com.ivanov.microservice_project.repository;

import com.ivanov.microservice_project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectId(Long projectId);

    List<Comment> findByTaskId(Long taskId);
    // Методы для работы с комментариями, если потребуется
}
